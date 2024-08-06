package com.hzdata.analytics.android.plugin.version

import com.google.gson.JsonObject
import com.hzdata.analytics.android.plugin.utils.TextUtil.isEmpty

class HZDataSDKVersionBean(
    /**
     * 其他业务 SDK 的版本信息路径，也就是 SensorDataVersionOptions 类的路径
     * 通过 SensorDataVersionOptions 类的路径匹配，当前依赖 SDK 的版本和已有的 SDK 的关系（每一个 SDK 唯一，用于匹配）
     * （必须）
     */
    val hzDataSDKPath: String,
    /**
     * 当前 SDK 要求的最小版本（必须）
     */
    private val mMinHZDataSDKVersion: String,
    /**
     * 版本不匹配时，提醒信息，可为 null
     */
    private val mMessage: String
) {

    fun getHZDataSDKVersionMessage(version:String): String {
        return if(!isVersionValid(version, mMinHZDataSDKVersion)) String.format(
            if (isEmpty(mMessage)) HZDataSDKVersionHelper.DEFAULT_MESSAGE else mMessage,
            version,
            mMinHZDataSDKVersion
        ) else ""
    }

    private fun isVersionValid(hzVersion: String, requiredVersion: String): Boolean {
        return try {
            if (hzVersion == requiredVersion) {
                true
            } else {
                val hzVersions = hzVersion.split(".").toTypedArray()
                val requiredVersions = requiredVersion.split(".").toTypedArray()
                for (index in requiredVersions.indices) {
                    val hzVersionsNum = hzVersions[index].toInt()
                    val requiredVersionsNum = requiredVersions[index].toInt()
                    if (hzVersionsNum != requiredVersionsNum) {
                        return hzVersionsNum > requiredVersionsNum
                    }
                }
                false
            }
        }catch (ex: Exception) {
            //ignore
            false
        }
    }

    override fun toString(): String {
        return """
            minHZDataSDKVersion: $mMinHZDataSDKVersion
            hzDataSDKPath: $hzDataSDKPath
            message: $mMessage
        """.trimIndent()
    }

    companion object {
        fun createHZDataSDKBean(jsonObject: JsonObject?): HZDataSDKVersionBean? {
            if (null != jsonObject) {
                try {
                    var path = jsonObject["SDK_VERSION_PATH"].asString
                    val minVersion = jsonObject["DEPENDENT_MIN_SDK_VERSIONS"].asString
                    if (!isEmpty(path) && !isEmpty(minVersion)) {
                        path = path.replace("\\.".toRegex(), "/")
                    }
                    val message = jsonObject["ERROR_MESSAGE"].asString
                    return HZDataSDKVersionBean(path, minVersion, message)
                }catch (e: Exception) {
                    //ignore
                }
            }
            return null
        }
    }
}