package com.hzdata.analytics.android.plugin.js

import com.hzdata.analytics.android.gradle.ClassInheritance
import com.hzdata.analytics.android.gradle.replaceSlashByDot
import com.hzdata.analytics.android.plugin.ClassNameAnalytics
import com.hzdata.analytics.android.plugin.utils.HZUtils
import com.hzdata.analytics.android.plugin.utils.Logger
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter


/**
 * 判断逻辑：
 *
 * 如果当前方法所在的类是 WebView 的子类，并且被处理的方法是目标方法中的一个就不处理；
 * 否则就判断 owner 是否是 WebView 的子类，如果是就处理，否则不处理。
 */
class HZAnalyticsWebViewMethodVisitor(
    api: Int,
    methodVisitor: MethodVisitor?,
    access: Int,
    name: String?,
    descriptor: String,
    private val classNameAnalytics: ClassNameAnalytics,
    private val classInheritance: ClassInheritance
) : AdviceAdapter(api, methodVisitor, access, name, descriptor) {

    //静态常量，用于标识JsBridge的API
    companion object {
        const val JS_BRIDGE_API = "com/hzdata/analytics/android/sdk/jsbridge/JSHookAop"
        val TARGET_NAME_DESC = mutableListOf(
            "loadUrl(LJava/lang/String;)V", "loadUrl(Ljava/lang/String;Ljava/util/Map;)V",
            "loadData(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
            "loadDataWithBaseURL(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
            "postUrl(Ljava/lang/String;[B)V"
        )
        const val VIEW_DESC = "Landroid/view/View;"
        val OWNER_WHITE_SET = mutableSetOf("android/webkit/WebView", "com/tencent/smtt/sdk/WebView")
    }

    private val methodNameDesc: String = name + descriptor
    private var shouldSkip = false

    init {
        //如果当前方法符合目标定义，而且此类是WebView子类，那么就跳过visitMethodInsn指令
        if (TARGET_NAME_DESC.contains(methodNameDesc) && isAssignableWebView(classNameAnalytics.className)) {
            shouldSkip = true
        }
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String,
        name: String,
        desc: String,
        itf: Boolean
    ) {
        if (shouldSkip) {
            super.visitMethodInsn(opcode, owner, name, desc, itf)
            return
        }
        try {
            if (opcode != INVOKESTATIC && TARGET_NAME_DESC.contains(name + desc)) {
                //解决NoClassDefError问题
                if (classNameAnalytics.superClass == "com/tenent/smtt/sdk/WebViewClient") {
                    super.visitMethodInsn(opcode, owner, name, desc, itf)
                    return
                }
                if (isAssignableWebView(owner)) {
                    val argTypes = Type.getArgumentTypes(desc)
                    val positionList = mutableListOf<Int>()
                    //依次复制操作数栈顶的元素到局部变量表中保存
                    for (element in argTypes) {
                        val position = newLocal(element)
                        storeLocal(position)
                        positionList.add(position)
                    }
                    val ownerPosition = newLocal(Type.getObjectType(owner))
                    storeLocal(ownerPosition)
                    positionList.add(ownerPosition)
                    //处理DCloud中WebView的写法，缩小范围为 AdaWebView
                    if (isDCloud(owner, classNameAnalytics.className)) {
                        hookDCloud(positionList, name, desc)
                    } else {
                        //将局部变量表中的数据压入操作数栈中触发我们需要插入的方法
                        positionList.reversed().forEach { tmp ->
                            loadLocal(tmp)
                        }
                        val newDesc = HZUtils.appendDescBeforeGiven(desc, VIEW_DESC)
                        mv.visitMethodInsn(INVOKESTATIC, JS_BRIDGE_API, name, newDesc, false)
                    }
                    //将局部变量表中的数据压入操作数栈中触发原有的方法
                    positionList.reversed().forEach { tmp ->
                        loadLocal(tmp)
                    }
                    super.visitMethodInsn(opcode, owner, name, desc, itf)
                    return
                }
            }
        } catch (throwable: Throwable) {
            Logger.warn("Can not auto handle webview, if you have any questions, please contact our technical services: classname:${classNameAnalytics.className}, method:${methodNameDesc}, exception: ${throwable}")
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf)
    }

    private fun isAssignableWebView(owner: String): Boolean {
        try {
            if (OWNER_WHITE_SET.contains(owner)) {
                return true
            }
            if (isDCloud(owner, classNameAnalytics.className)) {
                return true
            }
            return checkAndroidWebView(owner) || checkX5WebView(owner)
        } catch (throwable: Throwable) {
            Logger.warn("Can not load class, if you have any questions, please contact our technical services: classname:${classNameAnalytics.className}, exception: ${throwable}")
        }
        return false
    }

    private fun isDCloud(owner: String, className: String): Boolean {
        return owner == "io/dcloud/common/adapter/webview/DCWebView" && className.contains("AdaWebview")
    }

    private fun hookDCloud(positionList: MutableList<Int>, name: String, desc: String) {
        var isCask = false
        var tmpPosition = positionList.last()
        loadLocal(tmpPosition)
        mv.visitTypeInsn(INSTANCEOF, "android/view/View")
        val label = Label()
        mv.visitJumpInsn(IFEQ, label)
        positionList.reversed().forEach { tmp ->
            loadLocal(tmp)
            if (!isCask) {
                isCask = true
                mv.visitTypeInsn(CHECKCAST, "android/view/View")
            }
        }
        val newDesc = HZUtils.appendDescBeforeGiven(desc, VIEW_DESC)
        //为保持新 SDK 使用旧版插件问题，会使用新 SDK loadUrl 后缀的方法
        mv.visitMethodInsn(INVOKESTATIC, JS_BRIDGE_API, name, newDesc, false)
        mv.visitLabel(label)
    }

    private fun checkAndroidWebView(owner: String): Boolean {
        if (classInheritance.isAssignableForm(
                owner.replaceSlashByDot(),
                "android.webkit.WebView"
            )
        ) {
            OWNER_WHITE_SET.add(owner)
            return true
        }
        return false
    }

    private fun checkX5WebView(owner: String): Boolean {
        if (classInheritance.isAssignableForm(
                owner.replaceSlashByDot(),
                "com.tencent.smtt.sdk.WebView"
            )
        ) {
            OWNER_WHITE_SET.add(owner)
            return true
        }
        return false
    }
}