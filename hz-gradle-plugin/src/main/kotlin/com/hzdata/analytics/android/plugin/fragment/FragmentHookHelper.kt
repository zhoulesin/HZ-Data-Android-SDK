package com.hzdata.analytics.android.plugin.fragment

import com.hzdata.analytics.android.plugin.manager.HZPackageManager
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

object FragmentHookHelper {
    /**
     * add Fragment life callback
     */
    fun hookFragment(
        classVisitor: ClassVisitor,
        superName: String?,
        visitedFragMethods: Set<String?>
    ) {
        if (HZPackageManager.isInstanceOfFragment(superName)) {
            var mv: MethodVisitor
            // 添加剩下的方法，确保super.onHiddenChanged(hidden);等先被调用
            for ((key, methodCell) in HZFragmentHookConfig.FRAGMENT_METHODS) {
                if (visitedFragMethods.contains(key)) {
                    continue
                }
                mv = classVisitor.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    methodCell.name,
                    methodCell.desc,
                    null,
                    null
                )
                mv.visitCode()
                // call super
                methodCell.visitMethod(mv, Opcodes.INVOKESPECIAL, superName!!)
                // call injected method
                methodCell.visitHookMethod(
                    mv,
                    Opcodes.INVOKESTATIC,
                    HZFragmentHookConfig.SENSORS_FRAGMENT_TRACK_HELPER_API
                )
                mv.visitInsn(Opcodes.RETURN)
                mv.visitMaxs(methodCell.paramsCount, methodCell.paramsCount)
                mv.visitEnd()
                mv.visitAnnotation(
                    "Lcom/hzdata/analytics/android/sdk/HZDataInstrumented;",
                    false
                )
            }
        }
    }
}
