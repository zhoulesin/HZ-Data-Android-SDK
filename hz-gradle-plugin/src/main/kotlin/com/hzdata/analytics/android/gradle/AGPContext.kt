package com.hzdata.analytics.android.gradle

/**
 * AGPContext 接口定义了一个上下文，该上下文提供了与 ASM 兼容的工厂。
 * ASM 是一个 Java 字节码操纵和分析框架。
 */
interface AGPContext {
    /**
     * 获取 ASM 兼容的工厂实例。
     * 这个属性可能为 null，表示当前上下文不支持 ASM 兼容工厂。
     */
    val asmCompatFactory: AsmCompatFactory?
}

/**
 * ClassInheritance 接口提供了检查类继承关系和加载类信息的能力。
 */
interface ClassInheritance {
    /**
     * 检查一个类是否可以从另一个类派生。
     *
     * @param subClass 子类的全限定名。
     * @param superClass 超类的全限定名。
     * @return 如果 subClass 可以从 superClass 派生，则返回 true；否则返回 false。
     */
    fun isAssignableForm(subClass: String, superClass: String): Boolean

    /**
     * 加载指定类的信息。
     *
     * @param className 要加载的类的全限定名。
     * @return 如果类加载成功，返回类的信息；否则返回 null。
     */
    fun loadClass(className: String): ClassInfo?
}
