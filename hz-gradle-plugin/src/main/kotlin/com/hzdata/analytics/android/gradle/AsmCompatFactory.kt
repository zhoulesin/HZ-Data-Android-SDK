package com.hzdata.analytics.android.gradle

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

/**
 * AsmCompatFactory 是一个抽象类，定义了类访问者工厂的接口。
 * 它用于创建特定的 ClassVisitor 实例以处理类，判断类是否符合被仪器化的条件，并提供关于工厂本身的附加信息。
 */
abstract class AsmCompatFactory {

    /**
     * 创建并返回一个 ClassVisitor 实例。
     * 此方法在ASM类处理阶段被调用，目的是包裹给定的 ClassVisitor，添加自定义处理逻辑。
     *
     * @param classVisitor 本工厂创建的 ClassVisitor 将向其委托方法调用的 ClassVisitor。
     * @param classInheritance 关于类继承的信息，用于协助确定如何处理此类。
     * @return 返回一个新的 ClassVisitor 实例，该实例将用于进一步的类处理。
     */
    abstract fun transform(classVisitor: ClassVisitor, classInheritance: ClassInheritance): ClassVisitor

    /**
     * 判断工厂是否希望使用给定的[classInfo]来为类添加检测或修改逻辑。
     *
     * @param classInfo 类的相关信息，用于判断是否可以或需要进行类的仪器化处理。
     * @return 返回布尔值，表示该类是否应被此工厂处理。
     */
    abstract fun isInstrumentable(classInfo: ClassInfo): Boolean

    /**
     * 指示工厂是否支持或需要以增量方式构建或处理类。
     *
     * @return 布尔值，表示是否应采用增量构建模式。
     */
    abstract val isIncremental: Boolean

    /**
     * 提供此工厂的标识名称，通常对应于Transform的名称。
     *
     * @return 工厂的接口或名称字符串。
     */
    abstract val name: String

    /**
     * 在执行转换操作之前调用的方法，可用于初始化或设置操作前的状态。
     */
    abstract fun onBeforeTransform()

    /**
     * 默认使用的ASM API版本，可被子类覆盖以适应不同的ASM兼容需求。
     */
    open var asmAPI = Opcodes.ASM7

    /**
     * 提供此工厂扩展的附加数据或功能接口。
     * 具体类型由实现该抽象类的子类决定。
     *
     * @return 作为扩展功能的任意类型对象。
     */
    abstract val extension: Any
}
