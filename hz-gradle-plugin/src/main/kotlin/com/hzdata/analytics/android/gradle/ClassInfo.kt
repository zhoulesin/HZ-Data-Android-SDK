package com.hzdata.analytics.android.gradle

/**
 * 表示一个类的信息，包括其全限定名、实现的接口列表和超类列表。
 *
 * @param qualifiedName 类的全限定名，包括包名和类名。
 * @param interfaces 类实现的接口列表，可能为空。每个接口的全限定名被记录。
 * @param superClasses 类的超类列表，可能为空。每个超类的全限定名被记录。
 */
data class ClassInfo(
    val qualifiedName: String,
    val interfaces: List<String>? = null,
    val superClasses: List<String>? = null
)

