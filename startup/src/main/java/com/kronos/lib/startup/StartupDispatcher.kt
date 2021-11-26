package com.kronos.lib.startup

import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.kronos.lib.startup.thread.StartUpThreadFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */

internal class StartupDispatcher(executor: Executor? = null) {

    private val mExecutor = executor ?: Executors.newFixedThreadPool(4, StartUpThreadFactory())

    fun dispatch(
        context: Context,
        task: StartupTask,
        onCompleted: ((task: StartupTask) -> Unit)? = null
    ) {
        if (task.mainThread()) {
            execute(context, task)
            onCompleted?.invoke(task)
        } else {
            mExecutor.execute {
                execute(context, task)
                onCompleted?.invoke(task)
            }
        }
    }

    private fun execute(context: Context, task: StartupTask) {
        task.onTaskStart()
        log(task, "task start.")
        val start = SystemClock.elapsedRealtime()
        task.run(context)
        task.onTaskCompleted()
        val duration = SystemClock.elapsedRealtime() - start
        val tag = task.tag().takeIf { it.isNotBlank() } ?: task.javaClass.simpleName
        Log.i(COAST_TAG, "$tag: task completed. cost: ${duration}ms")
        log(task, "task completed. cost: ${duration}ms")
        track(task, duration)
    }

    companion object {
        private const val COAST_TAG = "Startup.Coast"
    }
}


