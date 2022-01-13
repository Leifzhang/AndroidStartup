package com.kronos.lib.startup.utils

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import android.text.TextUtils
import com.kronos.lib.startup.logger.KLogger
import java.io.BufferedReader
import java.io.FileReader

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/8
 *
 */
object ProcessUtils {

    private var procName: String? = null

    var application: Context? = null

    fun myProcName(): String? {
        if (!TextUtils.isEmpty(procName)) {
            return procName
        }
        procName = myProcName(requireNotNull(application))
        return procName
    }

    private fun myProcName(ctx: Context?): String? {
        var procName: String? = null
        //除了最后一个  基本都是安全的获取进程名
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            procName = Application.getProcessName()
        }
        if (TextUtils.isEmpty(procName) && ctx != null) {
            procName = getProcessNameByReflect(ctx)
        }
        if (TextUtils.isEmpty(procName)) {
            procName = processName()
        }
        if (TextUtils.isEmpty(procName) && ctx != null) {
            // 该方法有隐私合规风险
            procName = processName(ctx)
        }
        return procName
    }

    private fun getProcessNameByReflect(context: Context): String? {
        val activityThread = getActivityThread(context)
        if (activityThread != null) {
            try {
                val method =
                    activityThread.javaClass.getMethod("currentProcessName")
                method.isAccessible = true
                return method.invoke(activityThread) as String
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun getActivityThread(context: Context): Any? {
        var activityThread = getActivityThreadInActivityThreadStaticField()
        if (activityThread != null) return activityThread
        activityThread = getActivityThreadInActivityThreadStaticMethod()
        return activityThread ?: getActivityThreadInLoadedApkField(context)
    }

    private fun getActivityThreadInActivityThreadStaticField(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThreadField =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            sCurrentActivityThreadField.isAccessible = true
            sCurrentActivityThreadField[null]
        } catch (e: Exception) {
            KLogger.e("ProcessUtils", "getActivityThreadInActivityThreadStaticField: " + e.message)
            null
        }
    }

    private fun getActivityThreadInActivityThreadStaticMethod(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            activityThreadClass.getMethod("currentActivityThread").invoke(null)
        } catch (e: Exception) {
            KLogger.e("ProcessUtils", "getActivityThreadInActivityThreadStaticMethod: " + e.message)
            null
        }
    }

    private fun getActivityThreadInLoadedApkField(context: Context): Any? {
        return try {
            val mLoadedApkField = Application::class.java.getDeclaredField("mLoadedApk")
            mLoadedApkField.isAccessible = true
            val mLoadedApk = mLoadedApkField[context]
            val mActivityThreadField = mLoadedApk.javaClass.getDeclaredField("mActivityThread")
            mActivityThreadField.isAccessible = true
            mActivityThreadField[mLoadedApk]
        } catch (e: Exception) {
            KLogger.e("UtilsActivityLifecycle", "getActivityThreadInLoadedApkField: " + e.message)
            null
        }
    }

    //get from ActivityManager
    fun processName(ctx: Context): String? {
        try {
            val manager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
            if (manager != null) {
                val processInfoList = manager.runningAppProcesses
                if (processInfoList != null) {
                    for (processInfo in processInfoList) {
                        if (processInfo.pid == Process.myPid() && !TextUtils.isEmpty(processInfo.processName)) {
                            return processInfo.processName
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return null
    }

    private fun processName(): String? {
        return processName(Process.myPid())
    }

    //get from /proc/PID/cmdline
    private fun processName(pid: Int): String? {
        var br: BufferedReader? = null
        try {
            br = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = br.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
                if (!TextUtils.isEmpty(processName)) {
                    return processName
                }
            }
        } catch (ignored: Exception) {
        } finally {
            try {
                br?.close()
            } catch (ignored: Exception) {
            }
        }
        return null
    }

}


fun Context.isMainProc(proc: String?): Boolean {
    return packageName == proc
}

fun String?.isMainProc(): Boolean {
    return this?.contains(":") == false ?: false
}