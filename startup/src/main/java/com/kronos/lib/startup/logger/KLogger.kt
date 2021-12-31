package com.kronos.lib.startup.logger

import android.util.Log
import com.kronos.lib.startup.StartupConfig

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/27
 *
 */
object KLogger {

    private val debugLog: KLoggerImp = object : KLoggerImp {

        override fun v(tag: String, msg: String, vararg obj: Any?) {
            val log = if (obj.isEmpty()) msg else String.format(msg, *obj)
            Log.v(tag, log.appendThreadName())
        }

        override fun i(tag: String, msg: String, vararg obj: Any?) {
            val log = if (obj.isEmpty()) msg else String.format(msg, *obj)
            Log.i(tag, log.appendThreadName())
        }

        override fun d(tag: String, msg: String, vararg obj: Any?) {
            val log = if (obj.isEmpty()) msg else String.format(msg, *obj)
            Log.d(tag, log.appendThreadName())
        }

        override fun w(tag: String, msg: String, vararg obj: Any?) {
            val log = if (obj.isEmpty()) msg else String.format(msg, *obj)
            Log.w(tag, log.appendThreadName())
        }

        override fun e(tag: String, msg: String, vararg obj: Any?) {
            val log = if (obj.isEmpty()) msg else String.format(msg, *obj)
            Log.e(tag, log.appendThreadName())
        }

        override fun printErrStackTrace(
            tag: String,
            tr: Throwable?,
            format: String?,
            vararg obj: Any?
        ) {
            var log = if (obj.isEmpty()) format else String.format(format!!, *obj)
            if (log == null) {
                log = ""
            }
            log += "  " + Log.getStackTraceString(tr)
            Log.e(tag, log.appendThreadName())
        }
    }
    var impl: KLoggerImp = debugLog

    fun setKLoggerImp(imp: KLoggerImp) {
        impl = imp
    }

    @JvmStatic
    fun v(tag: String, msg: String, vararg obj: Any?) {
        if (StartupConfig.debugMode) {
            impl.v(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String, vararg obj: Any?) {
        if (StartupConfig.debugMode) {
            impl.e(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String, vararg obj: Any?) {
        if (StartupConfig.debugMode) {
            impl.w(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String, vararg obj: Any?) {
        if (StartupConfig.debugMode) {
            impl.i(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String, vararg obj: Any?) {
        if (StartupConfig.debugMode) {
            impl.d(tag, msg, *obj)
        }
    }

    @JvmStatic
    fun printErrStackTrace(tag: String, tr: Throwable?, format: String?, vararg obj: Any?) {
        if (StartupConfig.debugMode) {
            impl.printErrStackTrace(tag, tr, format, *obj)
        }
    }

    interface KLoggerImp {
        fun v(tag: String, msg: String, vararg obj: Any?)
        fun i(tag: String, msg: String, vararg obj: Any?)
        fun w(tag: String, msg: String, vararg obj: Any?)
        fun d(tag: String, msg: String, vararg obj: Any?)
        fun e(tag: String, msg: String, vararg obj: Any?)
        fun printErrStackTrace(tag: String, tr: Throwable?, format: String?, vararg obj: Any?)
    }
}

fun String.appendThreadName(): String {
    val threadName = Thread.currentThread().name
    return "[threadName:$threadName] [message:$this]"
}