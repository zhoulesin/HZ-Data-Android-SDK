package com.hzdata.analytics.android.plugin.configs

import com.hzdata.analytics.android.plugin.visitor.HZAnalyticsMethodCell
import org.objectweb.asm.Opcodes

object HZAnalyticsHookConfig {
    const val SENSORS_ANALYTICS_API =
        "com/hzdata/analytics/android/autotrack/aop/HZDataAutoTrackHelper"
    val INTERFACE_METHODS = mutableMapOf<String, HZAnalyticsMethodCell>()
    val CLASS_METHODS = mutableMapOf<String, HZAnalyticsMethodCell>()

    init {
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onCheckedChanged",
                "(Landroid/widget/CompoundButton;Z)V",
                "android/widget/CompoundButton\$OnCheckedChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onRatingChanged",
                "(Landroid/widget/RatingBar;FZ)V",
                "android/widget/RatingBar\$OnRatingBarChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onStopTrackingTouch",
                "(Landroid/widget/SeekBar;)V",
                "android/widget/SeekBar\$OnSeekBarChangeListener",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onCheckedChanged",
                "(Landroid/widget/RadioGroup;I)V",
                "android/widget/RadioGroup\$OnCheckedChangeListener",
                "trackRadioGroup",
                "(Landroid/widget/RadioGroup;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onClick",
                "(Landroid/content/DialogInterface;I)V",
                "android/content/DialogInterface\$OnClickListener",
                "trackDialog",
                "(Landroid/content/DialogInterface;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onItemSelected",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "android/widget/AdapterView\$OnItemSelectedListener",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z",
                "android/widget/ExpandableListView\$OnGroupClickListener",
                "trackExpandableListViewOnGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;I)V",
                1, 3,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z",
                "android/widget/ExpandableListView\$OnChildClickListener",
                "trackExpandableListViewOnChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;II)V",
                1, 4,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onTabChanged",
                "(Ljava/lang/String;)V",
                "android/widget/TabHost\$OnTabChangeListener",
                "trackTabHost",
                "(Ljava/lang/String;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onTabSelected",
                "(Landroid/support/design/widget/TabLayout\$Tab;)V",
                "android/support/design/widget/TabLayout\$OnTabSelectedListener",
                "trackTabLayoutSelected",
                "(Ljava/lang/Object;Ljava/lang/Object;)V",
                0, 2,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onTabSelected",
                "(Lcom/google/android/material/tabs/TabLayout\$Tab;)V",
                "com/google/android/material/tabs/TabLayout\$OnTabSelectedListener",
                "trackTabLayoutSelected",
                "(Ljava/lang/Object;Ljava/lang/Object;)V",
                0, 2,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "android/widget/Toolbar\$OnMenuItemClickListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "android/support/v7/widget/Toolbar\$OnMenuItemClickListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "androidx/appcompat/widget/Toolbar\$OnMenuItemClickListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onClick",
                "(Landroid/content/DialogInterface;IZ)V",
                "android/content/DialogInterface\$OnMultiChoiceClickListener",
                "trackDialog",
                "(Landroid/content/DialogInterface;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "android/widget/PopupMenu\$OnMenuItemClickListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "androidx/appcompat/widget/PopupMenu\$OnMenuItemClickListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "android/support/v7/widget/PopupMenu\$OnMenuItemClickListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "com/google/android/material/navigation/NavigationView\$OnNavigationItemSelectedListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "android/support/design/widget/NavigationView\$OnNavigationItemSelectedListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "android/support/design/widget/BottomNavigationView\$OnNavigationItemSelectedListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addInterfaceMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "com/google/android/material/bottomnavigation/BottomNavigationView\$OnNavigationItemSelectedListener",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
    }

    init {
        addClassMethod(
            HZAnalyticsMethodCell(
                "performClick",
                "()Z",
                "androidx/appcompat/widget/ActionMenuPresenter\$OverflowMenuButton",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                0, 1,
                listOf(Opcodes.ALOAD)
            )
        )

        addClassMethod(
            HZAnalyticsMethodCell(
                "performClick",
                "()Z",
                "android/support/v7/widget/ActionMenuPresenter\$OverflowMenuButton",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                0, 1,
                listOf(Opcodes.ALOAD)
            )
        )

        addClassMethod(
            HZAnalyticsMethodCell(
                "performClick",
                "()Z",
                "android/widget/ActionMenuPresenter\$OverflowMenuButton",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                0, 1,
                listOf(Opcodes.ALOAD)
            )
        )
    }

    private fun addInterfaceMethod(HZAnalyticsMethodCell: HZAnalyticsMethodCell?) {
        if (HZAnalyticsMethodCell != null) {
            INTERFACE_METHODS.put(
                HZAnalyticsMethodCell.parent + HZAnalyticsMethodCell.name + HZAnalyticsMethodCell.desc,
                HZAnalyticsMethodCell
            )
        }
    }

    private fun addClassMethod(HZAnalyticsMethodCell: HZAnalyticsMethodCell?) {
        if (HZAnalyticsMethodCell != null) {
            CLASS_METHODS.put(
                HZAnalyticsMethodCell.parent + HZAnalyticsMethodCell.name + HZAnalyticsMethodCell.desc,
                HZAnalyticsMethodCell
            )
        }
    }

    /**
     * android.gradle 3.2.1 版本中，针对 Lambda 表达式处理
     */

    val LAMBDA_METHODS = mutableMapOf<String, HZAnalyticsMethodCell>()

    //lambda 参数优化取样
    val SAMPLING_LAMBDA_METHODS = mutableListOf<HZAnalyticsMethodCell>()

    init {
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onClick",
                "(Landroid/view/View;)V",
                "Landroid/view/View\$OnClickListener;",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        SAMPLING_LAMBDA_METHODS.add(
            HZAnalyticsMethodCell(
                "onClick",
                "(Landroid/view/View;)V",
                "Landroid/view/View\$OnClickListener;",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onCheckedChanged",
                "(Landroid/widget/CompoundButton;Z)V",
                "Landroid/widget/CompoundButton\$OnCheckedChangeListener;",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onRatingChanged",
                "(Landroid/widget/RatingBar;FZ)V",
                "Landroid/widget/RatingBar\$OnRatingBarChangeListener;",
                "trackViewOnClick",
                "(Landroid/view/View;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onCheckedChanged",
                "(Landroid/widget/RadioGroup;I)V",
                "Landroid/widget/RadioGroup\$OnCheckedChangeListener;",
                "trackRadioGroup",
                "(Landroid/widget/RadioGroup;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        SAMPLING_LAMBDA_METHODS.add(
            HZAnalyticsMethodCell(
                "onCheckedChanged",
                "(Landroid/widget/RadioGroup;I)V",
                "Landroid/widget/RadioGroup\$OnCheckedChangeListener;",
                "trackRadioGroup",
                "(Landroid/widget/RadioGroup;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onClick",
                "(Landroid/content/DialogInterface;I)V",
                "Landroid/content/DialogInterface\$OnClickListener;",
                "trackDialog",
                "(Landroid/content/DialogInterface;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onItemClick",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "Landroid/widget/AdapterView\$OnItemClickListener;",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        SAMPLING_LAMBDA_METHODS.add(
            HZAnalyticsMethodCell(
                "onItemClick",
                "(Landroid/widget/AdapterView;Landroid/view/View;IJ)V",
                "Landroid/widget/AdapterView\$OnItemClickListener;",
                "trackListView",
                "(Landroid/widget/AdapterView;Landroid/view/View;I)V",
                1, 3,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z",
                "Landroid/widget/ExpandableListView\$OnGroupClickListener;",
                "trackExpandableListViewOnGroupClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;I)V",
                1, 3,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z",
                "Landroid/widget/ExpandableListView\$OnChildClickListener;",
                "trackExpandableListViewOnChildClick",
                "(Landroid/widget/ExpandableListView;Landroid/view/View;II)V",
                1, 4,
                listOf(Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onTabChanged",
                "(Ljava/lang/String;)V",
                "Landroid/widget/TabHost\$OnTabChangeListener;",
                "trackTabHost",
                "(Ljava/lang/String;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "Lcom/google/android/material/navigation/NavigationView\$OnNavigationItemSelectedListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "Landroid/support/design/widget/NavigationView\$OnNavigationItemSelectedListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "Landroid/support/design/widget/BottomNavigationView\$OnNavigationItemSelectedListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onNavigationItemSelected",
                "(Landroid/view/MenuItem;)Z",
                "Lcom/google/android/material/bottomnavigation/BottomNavigationView\$OnNavigationItemSelectedListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "Landroid/widget/Toolbar\$OnMenuItemClickListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "Landroid/support/v7/widget/Toolbar\$OnMenuItemClickListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "Landroidx/appcompat/widget/Toolbar\$OnMenuItemClickListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onClick",
                "(Landroid/content/DialogInterface;IZ)V",
                "Landroid/content/DialogInterface\$OnMultiChoiceClickListener;",
                "trackDialog",
                "(Landroid/content/DialogInterface;I)V",
                1, 2,
                listOf(Opcodes.ALOAD, Opcodes.ILOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "Landroid/widget/PopupMenu\$OnMenuItemClickListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "Landroidx/appcompat/widget/PopupMenu\$OnMenuItemClickListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )
        addLambdaMethod(
            HZAnalyticsMethodCell(
                "onMenuItemClick",
                "(Landroid/view/MenuItem;)Z",
                "Landroid/support/v7/widget/PopupMenu\$OnMenuItemClickListener;",
                "trackMenuItem",
                "(Landroid/view/MenuItem;)V",
                1, 1,
                listOf(Opcodes.ALOAD)
            )
        )

        // Todo: 扩展
    }

    private fun addLambdaMethod(HZAnalyticsMethodCell: HZAnalyticsMethodCell?) {
        if (HZAnalyticsMethodCell != null) {
            LAMBDA_METHODS.put(
                HZAnalyticsMethodCell.parent + HZAnalyticsMethodCell.name + HZAnalyticsMethodCell.desc,
                HZAnalyticsMethodCell
            )
        }
    }
}