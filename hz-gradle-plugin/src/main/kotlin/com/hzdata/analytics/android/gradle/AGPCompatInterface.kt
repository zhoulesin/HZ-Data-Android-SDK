package com.hzdata.analytics.android.gradle


/**
 * Represents different AGP version's base interface.
 * Different AGP should has it's own implementation.
 */
interface AGPCompatInterface {

    /**
     * current apg version
     */
    val agpVersion: AGPVersion

    /**
     * the main interface to handle ClassFile
     */
    val asmWrapperFactory: AsmCompatFactory
}