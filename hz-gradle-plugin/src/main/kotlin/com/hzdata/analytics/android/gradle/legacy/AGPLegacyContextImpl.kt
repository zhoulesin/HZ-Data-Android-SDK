package com.hzdata.analytics.android.gradle.legacy

import com.hzdata.analytics.android.gradle.AGPContext
import com.hzdata.analytics.android.gradle.AsmCompatFactory

object AGPLegacyContextImpl: AGPContext {
    override var asmCompatFactory: AsmCompatFactory? = null
}