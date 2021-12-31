package com.kronos.startup.dag.utils

import android.os.Handler
import android.os.Looper
import com.kronos.startup.dag.utils.MainThreadExecutor.Companion.mainThreadExecutor
import java.util.concurrent.Executor

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/30
 *
 */

internal class MainThreadExecutor internal constructor() : Executor {
    private val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mHandler.post(command)
    }

    companion object {
        internal val mainThreadExecutor by lazy {
            MainThreadExecutor()
        }
    }
}

fun postUI(invoke: Runnable) {
    mainThreadExecutor.execute(invoke)
}