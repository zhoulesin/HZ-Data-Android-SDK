package com.hzdata.analytics.android.plugin.push

import com.hzdata.analytics.android.plugin.utils.HZUtils.appendDescBeforeGiven
import com.hzdata.analytics.android.plugin.utils.HZUtils.isStatic
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import java.util.function.Consumer

/**
 * HZAnalyticsPushMethodVisitor 类是 ASM 库中的一个方法访问者，用于在特定方法调用前和调用后插入自定义代码。
 * 主要用于分析和推送功能的实现。
 *
 * @param api ASM 的 API 级别。
 * @param mMethodVisitor 方法访问者，用于实际修改字节码。
 * @param access 方法的访问标志。
 * @param name 方法的名称。
 * @param descriptor 方法的描述符。
 * @param superName 类的超类名称。
 */
class HZAnalyticsPushMethodVisitor(
    api: Int,
    private val mMethodVisitor: MethodVisitor,
    access: Int,
    name: String?,
    descriptor: String?,
    superName: String?
) : AdviceAdapter(
    api,
    mMethodVisitor, access, name, descriptor
) {

    // 方法名称和描述符的组合，用于唯一标识方法。
    private val mNameDesc: String = name + descriptor

    // 类的超类名称，用于某些逻辑判断。
    private val mSuperName: String? = superName

    /**
     * 在方法进入时执行的操作，主要用于插入推送逻辑代码。
     */
    override fun onMethodEnter() {
        super.onMethodEnter()
        // Hook Push
        // 非静态方法才进行推送处理
        if (!isStatic(methodAccess)) {
            HZPushInjected.handlePush(mMethodVisitor, mSuperName, mNameDesc)
        }
    }

    override fun visitCode() {
        super.visitCode()
    }

    /**
     * 当访问方法调用指令时执行的操作，主要用于插入PendingIntent和NotificationManager的方法调用前后的钩子代码。
     *
     * @param opcodeAndSource 操作码和源信息。
     * @param owner 方法定义的类名。
     * @param name 方法名。
     * @param descriptor 方法描述符。
     * @param isInterface 是否是接口方法。
     */
    override fun visitMethodInsn(
        opcodeAndSource: Int,
        owner: String,
        name: String,
        descriptor: String,
        isInterface: Boolean
    ) {
        // 检查是否为PendingIntent的静态方法调用
        //PendingIntent.getActivity() before and after
        if (opcodeAndSource == INVOKESTATIC && PENDING_INTENT_OWNER == owner
            && checkPendingIntentName(name)
        ) {
            val argTypes = Type.getArgumentTypes(descriptor)
            val positionList: ArrayList<Int> = ArrayList()
            for (index in argTypes.indices.reversed()) {
                val position = newLocal(argTypes[index])
                storeLocal(position, argTypes[index])
                positionList.add(0, position)
            }
            positionList.forEach(Consumer { local: Int? ->
                this.loadLocal(
                    local!!
                )
            })
            mv.visitMethodInsn(
                INVOKESTATIC,
                HZPushInjected.PUSH_TRACK_OWNER,
                getIntentHookMethodName(argTypes.size, name),
                refactorHookBeforeMethodDescriptor(descriptor),
                false
            )
            positionList.forEach(Consumer { local: Int? ->
                this.loadLocal(
                    local!!
                )
            })
            super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
            mv.visitInsn(DUP)
            positionList.forEach(Consumer { local: Int? ->
                this.loadLocal(
                    local!!
                )
            })
            mv.visitMethodInsn(
                INVOKESTATIC,
                HZPushInjected.PUSH_TRACK_OWNER,
                getPendingIntentHookMethodName(argTypes.size, name),
                refactorHookAfterMethodDescriptor(descriptor),
                false
            )
            return
        }

        // 检查是否为NotificationManager的notify方法调用
        //NotificationManager.notify()
        if (opcodeAndSource == INVOKEVIRTUAL && "android/app/NotificationManager" == owner && "notify" == name) {
            handleNotificationManagerHook(opcodeAndSource, owner, name, descriptor, isInterface)
            return
        }
        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
    }

    private fun handleNotificationManagerHook(
        opcodeAndSource: Int,
        owner: String,
        name: String,
        descriptor: String,
        isInterface: Boolean
    ) {
        val argTypes = Type.getArgumentTypes(descriptor)
        val positionList: ArrayList<Int> = ArrayList()
        for (index in argTypes.indices.reversed()) {
            val position = newLocal(argTypes[index])
            storeLocal(position, argTypes[index])
            positionList.add(0, position)
        }
        mv.visitInsn(DUP)
        positionList.forEach(Consumer { local: Int? ->
            this.loadLocal(
                local!!
            )
        })
        super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface)
        positionList.forEach(Consumer { local: Int? ->
            this.loadLocal(
                local!!
            )
        })
        mv.visitMethodInsn(
            INVOKESTATIC, HZPushInjected.PUSH_TRACK_OWNER, "onNotify",
            appendDescBeforeGiven(descriptor, "Landroid/app/NotificationManager;"), false
        )
    }

    /**
     * 检查方法名是否为PendingIntent的特定方法之一。
     *
     * @param methodName 方法名。
     * @return 如果方法名是PendingIntent的特定方法之一，则返回true；否则返回false。
     */
    private fun checkPendingIntentName(methodName: String): Boolean {
        return "getActivity" == methodName || "getService" == methodName || "getBroadcast" == methodName || "getForegroundService" == methodName
    }

    /**
     * 为PendingIntent的钩子方法生成修改后的描述符。
     *
     * @param desc 原始方法描述符。
     * @return 修改后的描述符。
     */
    private fun refactorHookBeforeMethodDescriptor(desc: String): String {
        //change return type to void
        //// 主要是为了将返回类型修改为void
        return desc.substring(0, desc.lastIndexOf(")") + 1) + "V"
    }

    /**
     * 为PendingIntent的后置钩子方法生成修改后的描述符。
     *
     * @param desc 原始方法描述符。
     * @return 修改后的描述符。
     */
    private fun refactorHookAfterMethodDescriptor(desc: String): String {
        //change return type to void and add PendingIntent prefix
        // 在描述符中添加PendingIntent参数，并将返回类型修改为void
        return "(Landroid/app/PendingIntent;" + desc.substring(1, desc.lastIndexOf(")") + 1) + "V"
    }

    /**
     * 根据方法名生成PendingIntent的钩子方法名。
     *
     * @param argsLength 参数数量。
     * @param name 方法名。
     * @return 钩子方法名。
     */
    private fun getIntentHookMethodName(argsLength: Int, name: String): String {
        return if ("getActivity" == name) {
            if (argsLength == 4) "hookIntentGetActivity" else "hookIntentGetActivityBundle"
        } else String.format("hookIntent%s", firstLetterUpper(name))
    }

    /**
     * 根据方法名生成PendingIntent的后置钩子方法名。
     *
     * @param argsLength 参数数量。
     * @param name 方法名。
     * @return 钩子方法名。
     */
    private fun getPendingIntentHookMethodName(argsLength: Int, name: String): String {
        return if ("getActivity" == name) {
            if (argsLength == 4) "hookPendingIntentGetActivity" else "hookPendingIntentGetActivityBundle"
        } else String.format("hookPendingIntent%s", firstLetterUpper(name))
    }

    companion object {
        private const val PENDING_INTENT_OWNER = "android/app/PendingIntent"
        private fun firstLetterUpper(name: String): String {
            name.uppercase()
            val cs = name.toCharArray()
            cs[0] = cs[0] - 32
            return String(cs)
        }
    }
}