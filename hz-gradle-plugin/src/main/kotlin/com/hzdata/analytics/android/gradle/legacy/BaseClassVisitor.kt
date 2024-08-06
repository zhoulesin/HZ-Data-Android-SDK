package com.hzdata.analytics.android.gradle.legacy

import org.objectweb.asm.ClassVisitor

/**
 * ClassVisitor 是一个用于分析、生成和转换 Java 字节码的类。它是 ASM（一个通用的 Java 字节码操作和分析框架）库中的一个核心组件。
 * ASM 提供了两个主要的功能：一个是用于生成字节码的 ClassWriter，另一个是用于分析现有字节码的 ClassReader。
 * ClassVisitor 则是这两个过程的桥梁，它定义了一系列方法，这些方法在字节码的每个重要部分被访问时会被调用。
 */
class BaseClassVisitor(api: Int, classVisitor: ClassVisitor?): ClassVisitor(api, classVisitor) {
}