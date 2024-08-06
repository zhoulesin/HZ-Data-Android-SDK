package com.hzdata.analytics.android.gradle

import com.hzdata.analytics.android.gradle.legacy.AGPLegacyImpl
import com.hzdata.analytics.android.gradle.v7_3.V73Impl
import org.gradle.api.Project

/**
 * 根据当前Android Gradle Plugin(AGP)的版本，加载相应的AGPCompatInterface实现。
 *
 * 此函数旨在提供对不同版本AGP的兼容性支持，通过动态选择合适的实现类，以适应AGP版本的变化。
 * 它避免了因AGP版本升级而导致的代码修改，简化了版本兼容性的处理。
 *
 * @param project 当前的Gradle项目对象，用于与项目进行交互。
 * @param asmWrapperFactory ASM兼容工厂，用于创建ASM相关的兼容封装类。
 * @return 返回一个AGPCompatInterface实例，该实例根据AGP版本不同，实现不同的功能。
 */
fun loadCompatImpl(project: Project, asmWrapperFactory: AsmCompatFactory): AGPCompatInterface {
    //获取当前AGP的版本号 android gradle plugin
    val version = AGPVersion.CURRENT_AGP_VERSION
    // 根据AGP版本号选择合适的实现类
    // 7.3.1及以上版本使用V73Impl，其他版本使用AGPLegacyImpl
    return if (version >= AGPVersion.AGP_7_3_1) {
        //为AGP 7.3.1及以上版本设计的实现。
        V73Impl(project, asmWrapperFactory)
    } else {
        //旧版本AGP设计的兼容性实现。
        AGPLegacyImpl(project, asmWrapperFactory)
    }
}
