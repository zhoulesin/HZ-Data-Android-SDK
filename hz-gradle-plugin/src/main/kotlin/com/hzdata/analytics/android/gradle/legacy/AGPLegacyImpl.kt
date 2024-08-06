package com.hzdata.analytics.android.gradle.legacy

import com.android.build.gradle.AppExtension
import com.hzdata.analytics.android.gradle.AGPCompatInterface
import com.hzdata.analytics.android.gradle.AGPVersion
import com.hzdata.analytics.android.gradle.AsmCompatFactory
import org.gradle.api.Project

@Suppress("DEPRECATION")
class AGPLegacyImpl(project: Project, override val asmWrapperFactory: AsmCompatFactory): AGPCompatInterface {
    init {
        if(project.plugins.hasPlugin("com.android.application")) {
            val android = project.extensions.findByType(AppExtension::class.java)
            android?.registerTransform(AGPLegacyTransform(asmWrapperFactory, android))
        }
    }

    override val agpVersion: AGPVersion
        get() = AGPVersion(7,3,0)
}