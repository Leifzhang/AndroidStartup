package com.kronos.lib.startup

import android.content.Context
import android.text.TextUtils

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class AnchorTaskWrap(
    private val task: StartupTask,
    private val anchorTaskName: MutableList<StartupTask>
) : StartupTask {

    override fun dependencies(): MutableList<String> {
        return task.dependencies().apply {
            anchorTaskName.forEach {
                val taskName = it.tag()
                if (contains(taskName)) return@apply
                if (!TextUtils.isEmpty(taskName)) {
                    add(taskName)
                }
            }

        }
    }

    override fun run(context: Context) {
        task.run(context)
    }

    override fun mainThread(): Boolean {
        return task.mainThread()
    }

    override fun await(): Boolean {
        return task.await()
    }

    override fun tag(): String {
        return task.tag()
    }

    override fun onTaskStart() {
        task.onTaskStart()
    }

    override fun onTaskCompleted() {
        task.onTaskCompleted()
    }

    override fun toString(): String {
        return "AnchorTaskWrap(task=$task, anchorTaskName=$anchorTaskName)"
    }

}