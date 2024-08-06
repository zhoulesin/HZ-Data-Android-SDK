package com.hzdata.analytics.android.gradle.v7_3

import com.android.build.api.instrumentation.FramesComputationMode
import com.android.build.api.instrumentation.InstrumentationScope
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.hzdata.analytics.android.gradle.AGPCompatInterface
import com.hzdata.analytics.android.gradle.AGPVersion
import com.hzdata.analytics.android.gradle.AsmCompatFactory
import org.gradle.api.Project
import kotlin.reflect.full.declaredMemberProperties

/**
 * 实现了AGPCompatInterface接口，用于兼容AGP（Android Gradle Plugin）7.3.1版本的特定行为。
 * 这个类主要负责在项目的构建过程中，对特定的变异（Variant）应用一系列的类转换，
 * 以实现对HZDataAsmClassVisitorFactory的定制化配置。
 */
class V73Impl(project: Project, override val asmWrapperFactory: AsmCompatFactory): AGPCompatInterface {

    /**
     * 返回AGP版本信息，固定为7.3.1。
     */
    override val agpVersion: AGPVersion
        get() = AGPVersion(7,3,1)

    /**
     * 初始化代码块，在项目初始化时执行。
     * 主要用于配置Android组件的扩展，并对每个变异应用特定的类转换器。
     *
     * @param project 当前的项目实例。
     */
    init {
        // 获取Android组件扩展，用于后续的配置。
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        // 设置ASM兼容工厂，用于后续的类转换。
        V73AGPContextImpl.asmCompatFactory = asmWrapperFactory

        // 对每个变异应用转换器。
        androidComponents.onVariants { variant: Variant ->
            // 应用HZDataAsmClassVisitorFactory类转换器。
            variant.instrumentation.transformClassesWith(
                HZDataAsmClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                // 获取并配置asmWrapperFactory的扩展属性。
                var extension = asmWrapperFactory.extension
                val memberProperties = extension::class.declaredMemberProperties
                memberProperties.forEach { property ->
                    // 根据属性名动态配置转换器的属性。
                    val result = property.getter.call(extension)
                    when(property.name) {
                        "useInclude" -> it.useInclude.set(result as Boolean)
                        "lambdaEnabled" -> it.lambdaEnabled.set(result as Boolean)
                        "addUCJavaScriptInterface" -> it.addUCJavaScriptInterface.set(result as Boolean)
                        "addXWalkJavaScriptInterface" -> it.addXWalkJavaScriptInterface.set(result as Boolean)
                        "lambdaParamOptimize" -> it.lambdaParamOptimize.set(result as Boolean)
                        "disableTrackKeyboard" -> it.disableTrackKeyboard.set(result as Boolean)
                        "exclude" -> it.exclude.set(result as MutableList<String>)
                        "include" -> it.include.set(result as MutableList<String>)
                        "disableModules" -> it.disableModules.set(result as MutableList<String>)
                    }

                    // 如果属性名为"sdk"，则进一步配置sdk的属性。
                    if (property.name == "sdk") {
                        val sdkResult = property.getter.call(extension)
                        val sdkProperties = sdkResult!!::class.declaredMemberProperties
                        sdkProperties.forEach { sdkProperty ->
                            // 根据sdk属性名动态配置转换器的sdk属性。
                            val sdkPropertyValue = sdkProperty.getter.call(sdkResult)
                            when(sdkProperty.name) {
                                "disableIMEI" -> it.disableIMEI.set(sdkPropertyValue as Boolean)
                                "disableLog" -> it.disableLog.set(sdkPropertyValue as Boolean)
                                "disableJsInterface" -> it.disableJsInterface.set(sdkPropertyValue as Boolean)
                                "disableAndroidID" -> it.disableAndroidID.set(sdkPropertyValue as Boolean)
                                "disableMacAddress" -> it.disableMacAddress.set(sdkPropertyValue as Boolean)
                                "disableCarrier" -> it.disableCarrier.set(sdkPropertyValue as Boolean)
                                "disableOAID" -> it.disableOAID.set(sdkPropertyValue as Boolean)
                            }
                        }
                    }
                }
            }

            // 设置ASM帧计算模式为COPY_FRAMES。
            variant.instrumentation.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }
}
