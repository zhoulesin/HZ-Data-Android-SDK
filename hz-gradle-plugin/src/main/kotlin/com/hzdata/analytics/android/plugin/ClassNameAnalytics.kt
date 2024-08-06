package com.hzdata.analytics.android.plugin

import com.hzdata.analytics.android.plugin.common.HookConstant
import com.hzdata.analytics.android.plugin.version.HZDataSDKVersionHelper

/**
 * 分析类名以确定其属于哪个类别的工具类。
 * 该类用于根据类名判断是否为特定的类或接口，主要用于数据分析和日志记录中。
 *
 * @param className 类的全限定名。
 * @param superClass 类的超类名，可能为空，用于判断类是否继承自特定类。
 * @param interfaces 类实现的接口列表，可能为空，用于判断类是否实现了特定接口。
 */
class ClassNameAnalytics(
    val className: String,
    val superClass: String?,
    val interfaces: List<String>? = null
) {
    /**
     * 判断类是否为HZ数据API类。
     */
    val isHZDataAPI: Boolean by lazy {
        className == HookConstant.HZ_DATA_API
    }

    /**
     * 判断类是否为HZ数据工具类。
     */
    val isHZDataUtils: Boolean by lazy {
        className == HookConstant.HZ_DATA_UTILS
    }

    /**
     * 判断类是否为OAID帮助类。
     */
    val isOAIDHelper by lazy {
        className == HookConstant.OAID_HELPER
    }

    /**
     * 判断类名是否以特定版本键结尾，用于识别HZ数据版本类。
     */
    val isHZDataVersion by lazy {
        className.endsWith(HZDataSDKVersionHelper.VERSION_KEY_SENSORDATA_VERSION_CONFIG)
    }

    /**
     * 判断类是否为HZ日志类。
     */
    val isHZLog by lazy {
        className == HookConstant.HZ_LOG
    }

    /**
     * 判断类是否为应用WebView接口类或其超类为视觉JS接口类。
     */
    val isAppWebViewInterface by lazy {
        className == HookConstant.APP_JS_INTERFACE || superClass == HookConstant.VISUAL_JS_INTERFACE
    }

    /**
     * 判断类名是否以特定包名开始并以"KeyboardViewUtil"结尾，用于识别键盘视图工具类。
     */
    val isKeyboardViewUtil by lazy {
        className.startsWith(PACKAGE_START) && className.endsWith(KEY_KEYBOARD)
    }

    companion object {
        /**
         * 键盘视图工具类的包名前缀。
         */
        private const val PACKAGE_START = "com/hzdata/analytics"
        /**
         * 键盘视图工具类的类名后缀。
         */
        private const val KEY_KEYBOARD = "KeyboardViewUtil"
    }

}
