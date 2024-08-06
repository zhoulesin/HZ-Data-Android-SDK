package com.hzdata.analytics.android.gradle.legacy

import com.hzdata.analytics.android.gradle.ClassInfo
import com.hzdata.analytics.android.gradle.ClassInheritance
import com.hzdata.analytics.android.gradle.replaceSlashByDot
import java.io.File
import java.net.URLClassLoader

/**
 * 继承自ClassInheritance的类，用于处理类加载器相关操作。
 */
class AGPClassInheritance: ClassInheritance {
    /**
     * URL类加载器，用于加载类。
     */
    private var urlclassloader : URLClassLoader? = null

    /**
     * 启动类路径，包含需要加载的类的文件。
     */
    val bootClassPath: MutableList<File> = mutableListOf()

    /**
     * 获取URL类加载器。
     * 如果当前urlclassloader为null，则根据bootClassPath创建一个新的URLClassLoader。
     * @return URLClassLoader 实例。
     */
    private fun getClassLoader(): URLClassLoader {
        if (urlclassloader == null) {
            urlclassloader = URLClassLoader(bootClassPath.map {
                it.toURI().toURL()
            }.toTypedArray())
        }
        return urlclassloader!!
    }

    /**
     * 判断一个类是否可以从另一个类或接口派生。
     * @param subClass 子类的全限定名。
     * @param superClass 父类或接口的全限定名。
     * @return 如果subClass可以从superClass派生，则返回true；否则返回false。
     */
    /**
     * 判断superClass是否是subClass的父类(或接口)
     */
    override fun isAssignableForm(subClass: String, superClass: String): Boolean {
        try {
            val subClazz = getClassLoader().loadClass(subClass.replaceSlashByDot())
            val superClazz = getClassLoader().loadClass(superClass.replaceSlashByDot())
            if (superClazz.isAssignableFrom(subClazz)) {
                return true
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            return false
        }
        return false
    }

    /**
     * 加载指定名称的类，并返回其相关信息。
     * @param className 类的全限定名。
     * @return 类的信息，包括类名、接口名和超类名；如果加载失败，则返回null。
     */
    override fun loadClass(className: String): ClassInfo? {
        return try {
            val clazz = getClassLoader().loadClass(className)
            val interfaces = clazz.interfaces.map {
                it.name
            }.toList()
            return if(clazz != null) ClassInfo(
                className,
                interfaces,
                mutableListOf(clazz.superclass.name)
            ) else null
        } catch (e: Throwable) {
            null
        }
    }
}
