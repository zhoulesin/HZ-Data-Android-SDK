package com.hzdata.analytics.android.plugin.visitor

import com.hzdata.analytics.android.gradle.ClassInheritance
import com.hzdata.analytics.android.plugin.ClassNameAnalytics
import com.hzdata.analytics.android.plugin.configs.HZConfigHookHelper
import com.hzdata.analytics.android.plugin.fragment.FragmentHookHelper
import com.hzdata.analytics.android.plugin.js.AddJSAnnotationVisitor
import com.hzdata.analytics.android.plugin.js.HZAnalyticsWebViewMethodVisitor
import com.hzdata.analytics.android.plugin.manager.HZModule
import com.hzdata.analytics.android.plugin.manager.HZPluginManager
import com.hzdata.analytics.android.plugin.push.HZAnalyticsPushMethodVisitor
import com.hzdata.analytics.android.plugin.push.HZPushInjected
import com.hzdata.analytics.android.plugin.version.HZAnalyticsVersionFieldVisitor
import com.hzdata.analytics.android.plugin.viewclick.HZAutoTrackMethodVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 自定义的ClassVisitor，用于处理和分析特定类的访问。
 *
 * @param classVisitor ASM的ClassVisitor，用于向下传递类访问事件。
 * @param pluginManager 插件管理器，用于管理不同模块的插件。
 * @param classInheritance 类继承关系助手，用于处理类继承相关的问题。
 */
class HZPrimaryClassVisitor(
    private val classVisitor: ClassVisitor,
    private val pluginManager: HZPluginManager,
    private val classInheritance: ClassInheritance
) : ClassVisitor(pluginManager.getASMVersion(), classVisitor) {

    // 类名分析器，用于分析类名及相关信息。
    private lateinit var classNameAnalytics: ClassNameAnalytics
    // 标志位，用于判断是否需要返回JSRAdapter。
    private var shouldReturnJSRAdapter = false
    // 标志位，用于标记是否在类中找到了onNewIntent方法。
    private var isFoundOnNewIntent = false

    // 存储已访问的Fragment方法名，用于处理自动跟踪。
    private val visitedFragMethods = mutableSetOf<String>()
    // 存储Lambda方法的相关信息。
    private val mLambdaMethodCells = mutableMapOf<String, HZAnalyticsMethodCell>()
    // 配置钩子助手，用于处理配置相关的钩子逻辑。
    private val configHookHelper = HZConfigHookHelper()

    /**
     * 处理类访问的开始。
     *
     * 初始化类名分析器，判断是否需要返回JSRAdapter，并初始化配置钩子。
     */
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        classNameAnalytics = ClassNameAnalytics(name, superName, interfaces?.asList())
        shouldReturnJSRAdapter = version <= Opcodes.V1_5
        configHookHelper.initConfigCellInClass(name)
    }

    /**
     * 处理字段访问。
     *
     * 如果字段属于特定的HZ数据分析API或版本字段，将使用特定的FieldVisitor进行处理。
     */
    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        //校验版本相关的操作
        val fieldVisitor = super.visitField(access, name, descriptor, signature, value)
        if (classNameAnalytics.isHZDataAPI || classNameAnalytics.isHZDataVersion) {
            return HZAnalyticsVersionFieldVisitor(
                pluginManager.getASMVersion(),
                fieldVisitor, name, value, pluginManager.sdkVersionHelper, classNameAnalytics
            )
        }
        return fieldVisitor
    }

    /**
     * 处理方法访问。
     *
     * 根据不同的条件和配置，可能需要应用不同的MethodVisitor来处理方法。
     */
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor? {
        //check whether need to delete this method. if the method is deleted,
        //a new method will be created at visitEnd()
        if (configHookHelper.isConfigsMethod(name, descriptor)) {
            return null
        }

        // 标记是否在Activity中找到了onNewIntent方法。
        if (classNameAnalytics.superClass == "android/app/Activity"
            && name == "onNewIntent" && descriptor == "(Landroid/content/Intent;)V"
        ) {
            isFoundOnNewIntent = true
        }

        var methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions)
        // 如果启用了WebView模块，应用WebView相关的方法访问器。
        if (pluginManager.isModuleEnable(HZModule.WEB_VIEW)) {
            methodVisitor =
                HZAnalyticsWebViewMethodVisitor(
                    pluginManager.getASMVersion(),
                    methodVisitor,
                    access,
                    name,
                    descriptor!!,
                    classNameAnalytics,
                    classInheritance
                )
        }

        // 如果启用了Push模块，应用Push相关的方法访问器。
        if (pluginManager.isModuleEnable(HZModule.PUSH)) {
            methodVisitor = HZAnalyticsPushMethodVisitor(
                pluginManager.getASMVersion(),
                methodVisitor,
                access,
                name,
                descriptor,
                classNameAnalytics.superClass
            )
        }

        // 如果类实现了AppWebViewInterface，添加JavaScript接口注解。
        if (classNameAnalytics.isAppWebViewInterface) {
            // add JavaScriptInterface
            methodVisitor =
                AddJSAnnotationVisitor(methodVisitor, access, name, descriptor, pluginManager)
        }

        // 如果启用了自动跟踪模块，应用自动跟踪相关的方法访问器。
        if (pluginManager.isModuleEnable(HZModule.AUTOTRACK)) {
            methodVisitor = HZAutoTrackMethodVisitor(
                methodVisitor, access, name!!, descriptor!!, classNameAnalytics,
                visitedFragMethods, mLambdaMethodCells, pluginManager
            )
        }

        // 更新SDK插件版本信息。
        methodVisitor =
            UpdateSDKPluginVersionMV(
                pluginManager.getASMVersion(),
                methodVisitor,
                access,
                name,
                descriptor,
                classNameAnalytics
            )

        // 如果需要返回JSRAdapter，应用JSRAdapter。
        if (shouldReturnJSRAdapter) {
            return HZAnalyticsJSRAdapter(
                pluginManager.getASMVersion(),
                methodVisitor,
                access,
                name,
                descriptor,
                signature,
                exceptions
            )
        }

        return methodVisitor
    }

    /**
     * 处理类访问的结束。
     *
     * 在结束时，根据配置和类的特性，可能需要添加额外的方法或进行其他处理。
     */
    override fun visitEnd() {
        super.visitEnd()

        // 如果启用了Push模块且类是Activity且未找到onNewIntent方法，则添加onNewIntent方法。
        //给 Activity 添加 onNewIntent，满足 push 业务需求
        if (pluginManager.isModuleEnable(HZModule.PUSH)
            && !isFoundOnNewIntent
            && classNameAnalytics.superClass == "android/app/Activity"
        ) {
            HZPushInjected.addOnNewIntent(classVisitor)
        }

        // 如果启用了自动跟踪模块，处理Fragment相关的方法。
        //为 Fragment 添加方法，满足生命周期定义
        if (pluginManager.isModuleEnable(HZModule.AUTOTRACK)) {
            FragmentHookHelper.hookFragment(
                classVisitor,
                classNameAnalytics.superClass,
                visitedFragMethods
            )
        }

        // 禁用特定的标识符方法。
        //添加需要置空的方法
        configHookHelper.disableIdentifierMethod(classVisitor)
    }

}
