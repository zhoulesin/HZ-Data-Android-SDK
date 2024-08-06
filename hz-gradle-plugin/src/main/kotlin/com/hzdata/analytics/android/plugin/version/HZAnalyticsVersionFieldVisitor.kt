package com.hzdata.analytics.android.plugin.version

import com.hzdata.analytics.android.plugin.ClassNameAnalytics
import com.hzdata.analytics.android.plugin.common.VersionConstant.MIN_SDK_VERSION
import com.hzdata.analytics.android.plugin.common.VersionConstant.VERSION
import com.hzdata.analytics.android.plugin.utils.HZUtils.compareVersion
import com.hzdata.analytics.android.plugin.utils.TextUtil.isEmpty
import org.objectweb.asm.FieldVisitor

class HZAnalyticsVersionFieldVisitor(
    api: Int,
    fieldVisitor: FieldVisitor?,
    private val mName: String?,
    private val mValue: Any?,
    private val mSdkVersionHelper: HZDataSDKVersionHelper,
    private val mClassNameAnalytics: ClassNameAnalytics
) : FieldVisitor(api, fieldVisitor) {

    override fun visitEnd() {
        if (mClassNameAnalytics.isHZDataAPI) {
            if ("VERSION" == mName) {
                val version = mValue as String
                if (compareVersion(MIN_SDK_VERSION, version) > 0) {
                    val errMessage = String.format(
                        "你目前集成的次声波埋点 SDK 版本号为 v%s, 请升级到 v%s 及以上版本。",
                        version,
                        MIN_SDK_VERSION
                    )
                    error(errMessage)
                    throw Error(errMessage)
                }
                val message = mSdkVersionHelper.getMessageBySDKCurrentVersion(
                    mClassNameAnalytics.className,
                    version
                )
                if (!isEmpty(message)) {
                    throw Error(message)
                }
            } else if ("MIN_PLUGIN_VERSION" == mName) {
                val minPluginVersion = mValue as String
                if (!isEmpty(minPluginVersion)) {
                    if (compareVersion(VERSION, minPluginVersion) < 0) {
                        val errMessage = String.format(
                            "你目前集成的次声波插件版本号为 v%s，请升级到 v%s 及以上的版本。",
                            VERSION,
                            minPluginVersion
                        )
                        error(errMessage)
                        throw Error(errMessage)
                    }
                }
            }
        } else if (mClassNameAnalytics.isHZDataVersion) {
            if (HZDataSDKVersionHelper.VERSION_KEY_CURRENT_VERSION == mName) {
                val version = mValue as String
                val message = mSdkVersionHelper.getMessageBySDKCurrentVersion(mClassNameAnalytics.className, version)
                if (!isEmpty(message)) {
                    throw Error(message)
                }
            } else if (HZDataSDKVersionHelper.VERSION_KEY_DEPENDENT_SDK_VERSION == mName) {
                val relatedOtherSDK = mValue as String
                val message = mSdkVersionHelper.getMessageBySDKRelyVersion(mClassNameAnalytics.className, relatedOtherSDK)
                if (!isEmpty(message)) {
                    throw Error(message)
                }
            }
        }
        super.visitEnd()
    }
}