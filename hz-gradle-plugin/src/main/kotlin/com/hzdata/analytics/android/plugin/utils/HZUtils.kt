package com.hzdata.analytics.android.plugin.utils

import org.objectweb.asm.Opcodes

object HZUtils {

    /**
     * 判断给定的访问权限是否包括公共访问权限。
     *
     * 使用位操作检查访问标志是否包含ACC_PUBLIC标志。该函数主要用于分析Java类或方法的访问权限。
     *
     * @param access 访问权限的整数值，包含各种访问标志，如ACC_PUBLIC、ACC_PRIVATE等。
     * @return 如果访问权限包括公共访问权限，则返回true；否则返回false。
     */
    fun isPublic(access: Int): Boolean {
        // 通过位与操作检查access是否包含ACC_PUBLIC标志
        return (access and Opcodes.ACC_PUBLIC) != 0;
    }

    /**
     * 判断给定的访问标志是否包含 ACC_STATIC 标志。
     *
     * 此函数用于解析 Java 字节码中的访问权限标志，特别是检查一个方法或字段是否被声明为静态的。
     * 静态成员属于类本身，而不是类的实例，因此它们在使用时不需要实例化类的对象。
     *
     * @param access 访问标志的整数值，包含各种访问权限信息，如 public、private、static 等。
     * @return 如果访问标志包含 ACC_STATIC 标志，则返回 true；否则返回 false。
     */
    fun isStatic(access: Int): Boolean {
        // 使用位与操作检查 access 是否包含 ACC_STATIC 标志。
        return (access and Opcodes.ACC_STATIC) != 0;
    }

    /**
     * 判断给定的访问权限是否包括 ACC_PROTECTED 标志。
     *
     * 此函数用于检查一个整型访问权限值中是否包含了 ACC_PROTECTED 标志。在Java和Kotlin中，
     * ACC_PROTECTED 标志用于表示类成员（如字段和方法）的保护访问权限。
     *
     * @param access 访问权限的整型表示，包含了各种访问权限标志，如 ACC_PUBLIC, ACC_PRIVATE, ACC_PROTECTED 等。
     * @return 如果访问权限中包含了 ACC_PROTECTED 标志，则返回true；否则返回false。
     */
    fun isProtected(access: Int): Boolean {
        // 使用位与操作检查 access 是否包含 ACC_PROTECTED 标志。
        return (access and Opcodes.ACC_PROTECTED) != 0;
    }

    /**
     * 比较两个字符串版本信息大小，例如 2.01.10 > 2.1.9.1.2
     *
     * @param version1 版本信息字符串
     * @param version2 版本信息字符串
     * @return 如果返回值为 0，表示版本相等；如果返回值为 1 表示 version1 大于 version2；如果返回值为 -1，表示 version1 小于 version2。
     */
    fun compareVersion(version1: String, version2: String): Int {
        val v1Array = version1.replace("-pre", "").split(".");
        val v2Array = version2.replace("-pre", "").split(".");
        val maxLength = Math.max(v1Array.size, v2Array.size);
        var str1: String?
        var str2: String?
        for (index in 0 until maxLength) {
            if (v1Array.size > index) {
                str1 = v1Array[index]
            } else {
                return -1;
            }
            if (v2Array.size > index) {
                str2 = v2Array[index];
            } else {
                return 1;
            }
            try {
                val num1 = Integer.valueOf(str1);
                val num2 = Integer.valueOf(str2);
                if (num1 != num2) {
                    return if (num1 - num2 > 0) 1 else -1
                }
            } catch (ignored: Exception) {
                return str1.compareTo(str2)
            }
        }
        return 0
    }

    /**
     * 获取 LOAD 或 STORE 的相反指令，例如 ILOAD => ISTORE，ASTORE => ALOAD
     *
     * @param LOAD 或 STORE 指令
     * @return 返回相对应的指令
     */
    fun convertOpcodes(code: Int): Int {
        var result = code
        when (code) {
            Opcodes.ILOAD -> result = Opcodes.ISTORE
            Opcodes.ALOAD -> result = Opcodes.ASTORE
            Opcodes.LLOAD ->
                result = Opcodes.LSTORE
            Opcodes.FLOAD ->
                result = Opcodes.FSTORE
            Opcodes.DLOAD ->
                result = Opcodes.DSTORE
            Opcodes.ISTORE ->
                result = Opcodes.ILOAD
            Opcodes.ASTORE ->
                result = Opcodes.ALOAD
            Opcodes.LSTORE ->
                result = Opcodes.LLOAD
            Opcodes.FSTORE ->
                result = Opcodes.FLOAD
            Opcodes.DSTORE ->
                result = Opcodes.DLOAD
        }
        return result
    }

    /**
     * 在给定描述的括号前添加额外的描述。
     * 该函数主要用于通过替换字符串中第一个括号的位置，来插入一段描述文字。
     * 这样做的目的是为了在不改变原有文本结构的前提下，添加额外的信息。
     *
     * @param givenDesc 原始描述字符串，假设该字符串中至少包含一个括号。
     * @param appendDesc 需要添加到原始描述前的额外描述。
     * @return 返回修改后的描述字符串，其中额外描述已被插入到第一个括号前。
     */
    fun appendDescBeforeGiven(givenDesc: String, appendDesc: String): String {
        // 通过正则表达式替换字符串中第一个括号前的内容，插入额外的描述。
        return givenDesc.replaceFirst("(", "($appendDesc")
    }

}