package com.hzdata.analytics.android.plugin.configs

import com.hzdata.analytics.android.plugin.common.HookConstant
import com.hzdata.analytics.android.plugin.extension.HZExtension
import com.hzdata.analytics.android.plugin.visitor.HZAnalyticsMethodCell
import org.objectweb.asm.ClassVisitor
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.HashMap

class HZConfigHookHelper() {

    //当前class文件对应的控制项， 单个class文件共用
    private var sClassInConfigCells = CopyOnWriteArrayList<HZAnalyticsMethodCell>()

    //扫描当前类命中的控制项
    private val mHookMethodCells = CopyOnWriteArrayList<HZAnalyticsMethodCell>()

    fun initConfigCellInClass(className: String) {
        sClassInConfigCells.clear()
        for(cell in mConfigCells.values) {
            if (cell.containsKey(className)) {
                sClassInConfigCells.addAll(cell[className]!!)
            }
        }
    }

    /**
     * 判断方法是不是 disablexx 配置方法
     */
    fun isConfigsMethod(name: String?, desc: String?): Boolean {
        for (methodCell in sClassInConfigCells) {
            if (methodCell.name == name && methodCell.desc == desc) {
                mHookMethodCells.add(methodCell)
                return true
            }
        }
        return false
    }


    /**
     * 清空方法体
     */
    fun disableIdentifierMethod(classVisitor: ClassVisitor) {
        for (cell in mHookMethodCells) {
            when (cell.agentName) {
                "createGetIMEI" -> {
                    HZAnalyticsSDKHookConfig.createGetIMEI(classVisitor, cell)
                }
                "createGetDeviceID" -> {
                    HZAnalyticsSDKHookConfig.createGetDeviceID(classVisitor, cell)
                }
                "createGetAndroidID" -> {
                    HZAnalyticsSDKHookConfig.createGetAndroidID(classVisitor, cell)
                }
                "createGetMacAddress" -> {
                    HZAnalyticsSDKHookConfig.createGetMacAddress(classVisitor, cell)
                }
                "createGetCarrier" -> {
                    HZAnalyticsSDKHookConfig.createGetCarrier(classVisitor, cell)
                }
                "createGetOAID" -> {
                    HZAnalyticsSDKHookConfig.createGetOAID(classVisitor, cell)
                }
                "createSALogInfo" -> {
                    HZAnalyticsSDKHookConfig.createSALogInfo(classVisitor, cell)
                }
                "createPrintStackTrack" -> {
                    HZAnalyticsSDKHookConfig.createPrintStackTrack(classVisitor, cell)
                }
                "createShowUpWebViewFour" -> {
                    HZAnalyticsSDKHookConfig.createShowUpWebViewFour(classVisitor, cell)
                }
                "createShowUpX5WebViewFour" -> {
                    HZAnalyticsSDKHookConfig.createShowUpX5WebViewFour(classVisitor, cell)
                }
                "createShowUpX5WebViewTwo" -> {
                    HZAnalyticsSDKHookConfig.createShowUpX5WebViewTwo(classVisitor, cell)
                }
            }
        }
    }

    companion object{
        // 插件配置项，全局共用
        val mConfigCells = HashMap<String, HashMap<String, List<HZAnalyticsMethodCell>>>()

        fun initSDKConfigCells(extension: HZExtension) {
            val sdkExtension = Objects.requireNonNull(extension).sdk
            mConfigCells.clear()
            if (sdkExtension.disableAndroidID) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.HZ_DATA_UTILS] =
                    HZAnalyticsSDKHookConfig.disableAndroidID()
                mConfigCells["disableAndroidID"] = cells
            }
            if (sdkExtension.disableCarrier) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.HZ_DATA_UTILS] = HZAnalyticsSDKHookConfig.disableCarrier()
                mConfigCells["disableCarrier"] = cells
            }
            if (sdkExtension.disableIMEI) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.HZ_DATA_UTILS] = HZAnalyticsSDKHookConfig.disableIMEI()
                mConfigCells["disableIMEI"] = cells
            }
            if (sdkExtension.disableMacAddress) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.HZ_DATA_UTILS] =
                    HZAnalyticsSDKHookConfig.disableMacAddress()
                mConfigCells["disableMacAddress"] = cells
            }
            if (sdkExtension.disableOAID) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.OAID_HELPER] = HZAnalyticsSDKHookConfig.disableOAID()
                mConfigCells["disableOAID"] = cells
            }
            if (sdkExtension.disableJsInterface) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.HZ_DATA_API] =
                    HZAnalyticsSDKHookConfig.disableJsInterface()
                mConfigCells["disableJsInterface"] = cells
            }
            if (sdkExtension.disableLog) {
                val cells = HashMap<String, List<HZAnalyticsMethodCell>>()
                cells[HookConstant.HZ_LOG] = HZAnalyticsSDKHookConfig.disableLog()
                mConfigCells["disableLog"] = cells
            }
        }
    }

}