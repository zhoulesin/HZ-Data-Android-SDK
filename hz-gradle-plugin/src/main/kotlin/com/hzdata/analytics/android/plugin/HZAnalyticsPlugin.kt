package com.hzdata.analytics.android.plugin

import com.hzdata.analytics.android.gradle.loadCompatImpl
import com.hzdata.analytics.android.plugin.manager.HZPluginManager
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * HZAnalyticsPlugin类实现了Plugin<Project>接口，用于应用到Gradle项目中。
 * 该插件的主要作用是加载兼容实现，以支持HZAnalytics的特定功能。
 */
class HZAnalyticsPlugin: Plugin<Project> {

    /**
     * apply方法被调用时，插件会被应用到指定的项目中。
     * @param project 要应用插件的Gradle项目对象。
     */
    override fun apply(project: Project) {
        // 打印日志，标识插件开始应用。
        println("HZAnalyticsPlugin222 applied")
        // 加载兼容实现，这里使用了HZPluginManager来管理插件的兼容性。
        loadCompatImpl(project, AsmCompatFactoryImpl(HZPluginManager(project)))
    }
}
