package com.hzdata.analytics.android.gradle.v7_3

import com.android.build.api.instrumentation.AsmClassVisitorFactory
import com.android.build.api.instrumentation.ClassContext
import com.android.build.api.instrumentation.ClassData
import com.android.build.api.instrumentation.InstrumentationContext
import com.android.build.api.instrumentation.InstrumentationParameters
import com.hzdata.analytics.android.gradle.ClassInfo
import com.hzdata.analytics.android.gradle.ClassInheritance
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor

/**
 * HZDataAsmClassVisitorFactory 类是用于创建 ClassVisitor 的工厂类。
 * 它是 AsmClassVisitorFactory 的具体实现，参数类型为 ConfigInstrumentParams。
 * 该类的主要作用是在字节码级别对类进行修改或增强，具体行为由其创建的 ClassVisitor 实现。
 */
abstract class HZDataAsmClassVisitorFactory : AsmClassVisitorFactory<ConfigInstrumentParams> {

    /**
     * 创建一个 ClassVisitor。
     *
     * @param classContext 类上下文，提供与类相关的操作和支持。
     * @param nextClassVisitor 接下来要访问该类的 ClassVisitor，当前创建的 ClassVisitor 需要作为其代理。
     * @return 返回一个实现了特定逻辑的 ClassVisitor。
     */
    override fun createClassVisitor(
        classContext: ClassContext,
        nextClassVisitor: ClassVisitor
    ): ClassVisitor {
        // 触发预转换操作，这可能是为了做一些准备工作或记录。
        V73AGPContextImpl.asmCompatFactory!!.onBeforeTransform()

        // 创建一个 ClassInheritance 实例，用于处理类继承和实现关系的查询。
        val classInheritance = object : ClassInheritance {
            /**
             * 判断一个子类是否可以赋值给一个超类。
             *
             * @param subClass 子类的全限定名。
             * @param superClass 超类的全限定名。
             * @return 如果子类可以赋值给超类，则返回 true，否则返回 false。
             */
            override fun isAssignableForm(subClass: String, superClass: String): Boolean {
                // 通过类上下文加载子类数据，并检查其是否满足超类条件。
                return classContext.loadClassData(subClass)?.let {
                    it.className == superClass || it.superClasses.contains(superClass) ||
                            it.interfaces.contains(superClass)
                } ?: false
            }

            /**
             * 加载指定类的信息。
             *
             * @param className 类的全限定名。
             * @return 返回类的信息，如果类不存在，则返回 null。
             */
            override fun loadClass(className: String): ClassInfo? {
                // 通过类上下文加载类数据，并构建 ClassInfo 实例。
                return classContext.loadClassData(className)?.let {
                    ClassInfo(
                        it.className,
                        interfaces = it.interfaces,
                        superClasses = it.superClasses
                    )
                }
            }
        }

        // 使用兼容工厂创建并返回一个 ClassVisitor，该 ClassVisitor 会应用特定的转换逻辑。
        return V73AGPContextImpl.asmCompatFactory!!.transform(
            nextClassVisitor, classInheritance
        )
    }

    /**
     * 判断一个类是否应该被增强或修改。
     *
     * @param classData 类的数据，包含类名、超类和接口等信息。
     * @return 如果类应该被增强或修改，则返回 true，否则返回 false。
     */
    override fun isInstrumentable(classData: ClassData): Boolean {
        // 使用兼容工厂判断类是否可增强。
        return V73AGPContextImpl.asmCompatFactory!!.isInstrumentable(
            ClassInfo(
                classData.className,
                interfaces = classData.interfaces,
                superClasses = classData.superClasses
            )
        )
    }
}

/**
 * ConfigInstrumentParams 接口定义了一组配置参数，用于指导类的字节码增强过程。
 * 它扩展了 InstrumentationParameters 接口，添加了更多具体的配置项。
 * 这些配置项主要用于控制增强行为的细节，比如是否启用某些功能或优化。
 */
interface ConfigInstrumentParams: InstrumentationParameters {
    // 定义了一系列属性，用于配置增强行为的各种选项。
    // 这里使用了 Gradle 的输入属性注解，表明这些属性是构建过程中的输入，会影响输出结果。

    @get:Input
    val useInclude: Property<Boolean>
    @get:Input
    val lambdaEnabled: Property<Boolean>
    @get:Input
    val addUCJavaScriptInterface: Property<Boolean>
    @get:Input
    val addXWalkJavaScriptInterface: Property<Boolean>
    @get:Input
    val lambdaParamOptimize: Property<Boolean>
    @get:Input
    val disableTrackKeyboard: Property<Boolean>
    @get:Input
    val exclude:ListProperty<String>
    @get:Input
    val include:ListProperty<String>
    @get:Input
    val disableModules:ListProperty<String>

    //SDK
    @get:Input
    val disableIMEI: Property<Boolean>
    @get:Input
    val disableLog: Property<Boolean>
    @get:Input
    val disableJsInterface: Property<Boolean>
    @get:Input
    val disableAndroidID: Property<Boolean>
    @get:Input
    val disableMacAddress: Property<Boolean>
    @get:Input
    val disableCarrier: Property<Boolean>
    @get:Input
    val disableOAID: Property<Boolean>
}
