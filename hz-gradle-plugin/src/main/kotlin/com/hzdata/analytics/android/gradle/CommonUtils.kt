package com.hzdata.analytics.android.gradle


/**
 * 根据类名加载类。
 *
 * 尝试使用Class.forName方法加载指定名称的类。如果类无法加载，例如类不存在或路径不正确，则返回null。
 * 这个函数对处理反射时的类名格式化很有帮助。
 *
 * @param className 类的全限定名，包括包名和类名，使用"."分隔。
 * @return 加载成功的类对象，或者在加载失败时返回null。
 */
fun loadClass(className: String) =
    try {
        Class.forName(className)
    } catch (e: Throwable) {
        null
    }

/**
 * 将字符串中的斜杠("/")替换为点(".")。
 *
 * 这个函数通常用于将文件路径转换为类名，因为在Java类路径中，斜杠用于分隔包名和类名。
 *
 * @return 替换后的字符串，其中所有斜杠都被点替换。
 */
fun String.replaceSlashByDot(): String {
    return this.replace(
        "/",
        "."
    )
}

/**
 * 将字符串中的点(".")替换为斜杠("/")。
 *
 * 这个函数的作用与replaceSlashByDot相反，它通常用于将类名转换为文件路径。
 * 在文件系统中，斜杠用于分隔目录名。
 *
 * @return 替换后的字符串，其中所有点都被斜杠替换。
 */
fun String.replaceDotBySlash(): String {
    return this.replace(
        ".",
        "/"
    )
}
