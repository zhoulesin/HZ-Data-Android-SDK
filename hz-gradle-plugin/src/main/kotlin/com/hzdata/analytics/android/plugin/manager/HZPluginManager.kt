package com.hzdata.analytics.android.plugin.manager

import com.hzdata.analytics.android.gradle.AGPVersion
import com.hzdata.analytics.android.plugin.configs.HZConfigHookHelper
import com.hzdata.analytics.android.plugin.extension.HZExtension
import com.hzdata.analytics.android.plugin.utils.Logger
import com.hzdata.analytics.android.plugin.version.HZDataSDKVersionHelper
import org.gradle.api.Project
import org.objectweb.asm.Opcodes
/**
 * 神策插件管理器，负责管理插件的配置和初始化。
 */
class HZPluginManager(private val project: Project) {
    /* 插件扩展配置 */
    lateinit var extension: HZExtension
    /* 是否启用方法进入钩子 */
    var isHookOnMethodEnter = false
    /* 是否为Android TV项目 */
    var isAndroidTV = false
    /* ASM版本号 */
    private var asmVersion = "ASM7"
    /* 包管理器，负责管理依赖的SDK和AAR */
    val packageManager: HZPackageManager by lazy {
        HZPackageManager().apply {
            exclude.addAll(extension.exclude)
            include.addAll(extension.include)
        }
    }
    /* 不支持的模块集合 */
    private val supportedModules: Set<HZModule> by lazy {
        val set = mutableSetOf<HZModule>()
        extension.disableModules.forEach {
            set.add(HZModule.valueOf(it.uppercase()))
        }
        set
    }

    /* 初始化插件管理器 */
    init {
        createExtension()
        parseProperty()
        checkDependency()
        otherInit()
    }

    /* 数据SDK版本助手 */
    val sdkVersionHelper: HZDataSDKVersionHelper by lazy {
        HZDataSDKVersionHelper()
    }

    /* 其他初始化工作，如初始化SDK配置 */
    private fun otherInit() {
        project.afterEvaluate {
            HZConfigHookHelper.initSDKConfigCells(extension)
            Logger.debug = extension.debug
        }
    }

    /* 检查项目依赖，确保必要的插件和依赖已正确配置 */
    private fun checkDependency() {
        project.afterEvaluate {
            if (!project.plugins.hasPlugin("com.android.application")) {
                check(false) {
                    "HZData Android Plugin must used at Android App Project"
                }
            }
            /*//需要充分考虑到模块中依赖 sdk 和 aar 依赖
            val dependencies = project.configurations.flatMap { it.dependencies }
            dependencies.forEach {
                val name = it.name
                val group = it.group
                val version = it.version
                if ("SensorsAnalyticsSDK" == name) {
                    isAutoTrackInstall = true
                    isPushModuleInstall = true
                    if (version != null && version != "unspecified" && group == "com.sensorsdata.analytics.android") {
                        check(
                            SensorsAnalyticsUtil.compareVersion(
                                VersionConstant.MIN_SDK_VERSION,
                                version
                            ) <= 0
                        ) {
                            "你目前集成的神策插件版本号为 v$version，请升级到 v${VersionConstant.MIN_SDK_VERSION} 及以上的版本。" +
                                    "详情请参考：https://github.com/sensorsdata/sa-sdk-android-plugin2"
                        }
                    }
                }
            }*/
        }
    }

    /* 解析项目属性，用于配置插件的行为 */
    private fun parseProperty() {
        project.properties.apply {
            isHookOnMethodEnter =
                (getOrDefault("hzAnalytics.isHookOnMethodEnter", "") as String).toBoolean()
            isAndroidTV = (getOrDefault("hzAnalytics.isAndroidTV", "false") as String).toBoolean()
            asmVersion = getOrDefault("hzAnalytics.asmVersion", "ASM7") as String
            if (AGPVersion.CURRENT_AGP_VERSION >= AGPVersion(8, 0, 0)) {
                asmVersion = "ASM9"
            }
        }
    }

    /* 创建插件的扩展配置 */
    private fun createExtension() {
        extension = project.extensions.create("hzAnalytics", HZExtension::class.java)
    }

    /* 获取ASM版本号对应的整数值 */
    fun getASMVersion(): Int {
        return when (asmVersion) {
            "ASM6" -> Opcodes.ASM6
            "ASM7" -> Opcodes.ASM7
            "ASM8" -> Opcodes.ASM8
            "ASM9" -> Opcodes.ASM9
            else -> Opcodes.ASM7
        }
    }

    /* 获取ASM版本号字符串 */
    internal fun getASMVersionStr(): String = asmVersion

    /* 检查给定模块是否启用 */
    fun isModuleEnable(module: HZModule) = !supportedModules.contains(module)
}

/**
 * 神策插件支持的模块枚举。
 */
enum class HZModule {
    AUTOTRACK, PUSH, WEB_VIEW, REACT_NATIVE
}
