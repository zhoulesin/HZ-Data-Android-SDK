package com.hzdata.analytics.android.plugin

import com.hzdata.analytics.android.gradle.AsmCompatFactory
import com.hzdata.analytics.android.gradle.ClassInfo
import com.hzdata.analytics.android.gradle.ClassInheritance
import com.hzdata.analytics.android.plugin.manager.HZPluginManager
import com.hzdata.analytics.android.plugin.utils.Logger
import com.hzdata.analytics.android.plugin.visitor.HZPrimaryClassVisitor
import org.objectweb.asm.ClassVisitor

/**
 * AsmCompatFactoryImpl 类实现了 AsmCompatFactory 接口，用于在字节码转换过程中提供兼容性支持。
 * 它主要负责创建适当的 ClassVisitor，并确定哪些类应该被转换。
 *
 * @param pluginManager 提供插件管理和配置的实例。
 */
class AsmCompatFactoryImpl(private val pluginManager: HZPluginManager) : AsmCompatFactory() {

    // 标志位，用于标记是否已经初始化过。
    private var isInit = false

    /**
     * 创建并返回一个修改过的 ClassVisitor。
     * 这个方法在类文件转换过程中被调用，用于链式处理类的字节码。
     *
     * @param classVisitor 接收处理后的类信息的访问器。
     * @param classInheritance 类的继承信息。
     * @return 返回一个定制的 ClassVisitor，用于进一步处理类的字节码。
     */
    override fun transform(
        classVisitor: ClassVisitor,
        classInheritance: ClassInheritance
    ): ClassVisitor {
        return HZPrimaryClassVisitor(classVisitor, pluginManager, classInheritance)
    }

    /**
     * 判断给定的类是否应该被转换。
     * 这个方法用于在决定是否对类进行字节码注入之前进行检查。
     *
     * @param classInfo 待检查的类的信息。
     * @return 如果类应该被转换，则返回 true；否则返回 false。
     */
    override fun isInstrumentable(classInfo: ClassInfo): Boolean {
        val instrument = pluginManager.packageManager.isInstrument(
            classInfo.qualifiedName,
            pluginManager.extension
        )
        if (instrument) {
            //需要转换的类
            println("isInstrumentable ${classInfo}")
        }
        return instrument
    }

    // 声明插件是否支持增量编译。
    override val isIncremental: Boolean
        get() = true

    // 插件的名称。
    override val name: String
        get() = "HZAnalyticsAutoTrack"

    /**
     * 在转换开始之前执行的同步方法。
     * 用于确保初始化只执行一次，并打印插件的配置信息。
     */
    @Synchronized
    override fun onBeforeTransform() {
        if (!isInit) {
            isInit = true
            printPluginInfo()
        }
    }

    // 使用插件管理器提供的ASM版本。
    override var asmAPI: Int = pluginManager.getASMVersion()

    // 获取插件的扩展配置。
    override val extension: Any
        get() = pluginManager.extension

    /**
     * 打印插件的配置信息。
     * 这包括版权信息、配置细节、是否钩子方法进入、是否支持Android TV以及ASM版本等。
     */
    private fun printPluginInfo() {
        Logger.printCopyright()
        Logger.info("plugin config detail:\nsensorsAnalytics {\n ${pluginManager.extension} \n}")
        Logger.info("是否在方法进入时插入代码: ${pluginManager.isHookOnMethodEnter}")
        Logger.info("是否添加 TV 支持: ${pluginManager.isAndroidTV}")
        Logger.info("ASM 版本为: ${pluginManager.getASMVersionStr()}")
        Logger.printNoLimit("")
    }
}
