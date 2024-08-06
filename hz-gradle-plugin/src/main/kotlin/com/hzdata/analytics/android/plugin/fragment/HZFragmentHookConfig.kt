package com.hzdata.analytics.android.plugin.fragment

import com.hzdata.analytics.android.plugin.visitor.HZAnalyticsMethodCell
import org.objectweb.asm.Opcodes
import java.util.Arrays

object HZFragmentHookConfig {
    const val SENSORS_FRAGMENT_TRACK_HELPER_API =
        "com/hzdata/analytics/android/autotrack/aop/FragmentTrackHelper"

    /**
     * Fragment中的方法
     */
    val FRAGMENT_METHODS = HashMap<String, HZAnalyticsMethodCell>()

    init {
        FRAGMENT_METHODS["onResume()V"] = HZAnalyticsMethodCell(
            "onResume",
            "()V",
            "",  // parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
            "trackFragmentResume",
            "(Ljava/lang/Object;)V",
            0, 1, listOf(Opcodes.ALOAD)
        )
        FRAGMENT_METHODS["setUserVisibleHint(Z)V"] =
            HZAnalyticsMethodCell(
                "setUserVisibleHint",
                "(Z)V",
                "",  // parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                "trackFragmentSetUserVisibleHint",
                "(Ljava/lang/Object;Z)V",
                0, 2,
                Arrays.asList(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        FRAGMENT_METHODS["onHiddenChanged(Z)V"] =
            HZAnalyticsMethodCell(
                "onHiddenChanged",
                "(Z)V",
                "",
                "trackOnHiddenChanged",
                "(Ljava/lang/Object;Z)V",
                0, 2,
                Arrays.asList(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        FRAGMENT_METHODS["onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V"] =
            HZAnalyticsMethodCell(
                "onViewCreated",
                "(Landroid/view/View;Landroid/os/Bundle;)V",
                "",
                "onFragmentViewCreated",
                "(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V",
                0, 3,
                Arrays.asList(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD)
            )
        FRAGMENT_METHODS["onPause()V"] = HZAnalyticsMethodCell(
            "onPause",
            "()V",
            "",  // parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
            "trackFragmentPause",
            "(Ljava/lang/Object;)V",
            0, 1, listOf(Opcodes.ALOAD)
        )
    }
}