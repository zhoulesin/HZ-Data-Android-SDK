package com.hzdata.analytics.android.gradle

/**
 * 表示Android Gradle插件(AGP)的版本号类，用于比较和标识不同版本。
 */
class AGPVersion(val major: Int, private val minor: Int, private val micro: Int) :
    Comparable<AGPVersion> {
    /**
     * 比较当前AGPVersion对象与另一个AGPVersion对象的大小。
     * 优先比较主要版本号，然后是次要版本号，最后是微版本号。
     *
     * @param other 要比较的另一个AGPVersion对象。
     * @return 返回一个整数，表示两个版本号的大小关系。如果当前版本大于other版本，返回正数；
     * 如果当前版本小于other版本，返回负数；如果两个版本相等，返回0。
     */
    override fun compareTo(other: AGPVersion): Int {
        var delta = this.major - other.major
        if (delta != 0) {
            return delta
        }
        delta = this.minor - other.minor
        if (delta != 0) {
            return delta
        }
        delta = this.micro - other.micro
        if (delta != 0) {
            return delta
        }
        return 0
    }

    /**
     * AGP的当前版本，通过延迟初始化获取。
     */
    companion object {
        val CURRENT_AGP_VERSION: AGPVersion by lazy {
            parseVersion()
        }

        /**
         * 版本号为7.3.1的AGP版本，作为一个具体的版本实例。
         */
        val AGP_7_3_1: AGPVersion by lazy {
            AGPVersion(7, 3, 1)
        }

        /**
         * 计算并返回Android Gradle插件的版本字符串。
         *
         * @return Android Gradle插件的版本字符串。
         */
        private fun calVertionStr(): String {
            var clazz =
                loadClass("com.android.Version") ?: loadClass("com.android.builder.model.Version")
            clazz?.apply {
                return getDeclaredField("ANDROID_GRADLE_PLUGIN_VERSION").get(this).toString()
            }
            error("Could not find Android Gradle Plugin Version")
        }

        /**
         * 解析版本字符串，返回一个AGPVersion对象。
         *
         * @return 解析后的AGPVersion对象。
         */
        private fun parseVersion(): AGPVersion {
            val vs = calVertionStr()
            println("parse Version ${vs}")
            val list: List<String> = vs.split(".")
            val result = "\\d*".toRegex().find(list[2])
            return AGPVersion(list[0].toInt(), list[1].toInt(), result?.value?.toInt() ?: 0)
        }
    }
}
