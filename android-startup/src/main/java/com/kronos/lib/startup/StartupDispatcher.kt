package com.kronos.lib.startup

import android.content.Context
import android.os.SystemClock
import android.util.Log
import java.util.concurrent.Executors

/**
 * @author : windfall
 * @date : 2021/6/16
 * @mail : liuchangjiang@bilibili.com
 */
internal class StartupDispatcher {

    private val executor = Executors.newFixedThreadPool(2)

    fun dispatch(
        context: Context,
        task: StartupTask,
        onCompleted: ((task: StartupTask) -> Unit)? = null
    ) {
        if (task.mainThread()) {
            execute(context, task)
            onCompleted?.invoke(task)
        } else {
            executor.execute {
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
        val tag = task.tag()?.takeIf { it.isNotBlank() } ?: task.javaClass.simpleName
        Log.i(COAST_TAG, "$tag: task completed. cost: ${duration}ms")
        log(task, "task completed. cost: ${duration}ms")
        track(task, duration)

    }

    companion object {
        private const val COAST_TAG = "Startup.Coast"
    }
}

