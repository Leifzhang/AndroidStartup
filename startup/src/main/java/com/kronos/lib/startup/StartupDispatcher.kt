package com.kronos.lib.startup

import android.content.Context
import com.kronos.lib.startup.thread.StartUpThreadFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */

internal class StartupDispatcher(executor: Executor? = null, val taskManager: StartupTaskManager) {

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
        task.run(context)
        task.onTaskCompleted()
    }

    companion object {
        const val COAST_TAG = "Startup.Coast"
    }
}


