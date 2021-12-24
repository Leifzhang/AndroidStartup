package com.kronos.startup.dag.data

import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.startup.dag.utils.getValueByDefault
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.abs

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/24
 *
 */
class ThreadTaskData(
    val mainTaskData: StartupTaskData
) {

    val startTask = hashMapOf<String, MutableList<StartupTaskData>>()
    val endChildTask = hashMapOf<String, MutableList<StartupTaskData>>()

    fun bindMainTask(taskInfo: StartupTaskData) {
        if (mainTaskData.begin - taskInfo.begin <= THREAD_DURATION_GAP) {
            startTask.getValueByDefault(taskInfo.threadName ?: "") {
                mutableListOf()
            }.apply {
                add(taskInfo)
            }
        }
    }



    fun isTaskEnd(tasks: CopyOnWriteArrayList<StartupTaskData>) {
        tasks.forEach { taskInfo ->
            if (abs(mainTaskData.getTaskEndTime() - taskInfo.getTaskEndTime()) <= THREAD_DURATION_GAP) {
                endChildTask.getValueByDefault(taskInfo.threadName ?: "") {
                    mutableListOf()
                }.apply {
                    add(taskInfo)
                }
                tasks.remove(taskInfo)
            }
        }

    }

    companion object {
        private const val THREAD_DURATION_GAP = 100
    }
}

