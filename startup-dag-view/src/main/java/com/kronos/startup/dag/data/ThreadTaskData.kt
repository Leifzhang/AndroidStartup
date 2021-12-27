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

    val startTask = hashMapOf<String, MutableList<StartupTaskData>>().apply {
        put(mainTaskData.threadName ?: "", mutableListOf(mainTaskData))
    }

    val endChildTask = hashMapOf<String, MutableList<StartupTaskData>>().apply {
        put(mainTaskData.threadName ?: "", mutableListOf(mainTaskData))
    }

    fun bindMainTask(taskInfo: StartupTaskData) {
        if (mainTaskData.begin - taskInfo.begin <= THREAD_DURATION_GAP) {
            startTask.getValueByDefault(taskInfo.threadName ?: "") {
                mutableListOf()
            }.apply {
                add(taskInfo)
            }
        }
    }


    fun getStartTaskInfo(): String {
        val stringBuilder = StringBuilder()
        if (startTask.isEmpty()) {
            return ""
        }
        stringBuilder.append("taskStart\r\n")
        startTask.forEach {
            stringBuilder.append("thread:").append(it.key).append("  taskName:")
            it.value.forEach { task ->
                stringBuilder.append(task.taskName).append(" ")
            }
            stringBuilder.append("\r\n")
        }
        return stringBuilder.toString()
    }


    fun getEndTaskInfo(): String {
        val stringBuilder = StringBuilder()
        if (endChildTask.isEmpty()) {
            return ""
        }
        stringBuilder.append("taskEnd\r\n")
        endChildTask.forEach {
            stringBuilder.append("thread:").append(it.key).append("  taskName:")
            it.value.forEach { task ->
                stringBuilder.append(task.taskName).append(" ")
            }
            stringBuilder.append("\r\n")
        }
        return stringBuilder.toString()
    }

    fun isTaskEnd(tasks: CopyOnWriteArrayList<StartupTaskData>) {
        tasks.forEach { taskInfo ->
            if (abs(mainTaskData.getTaskEndTime() - taskInfo.getTaskEndTime()) <= mainTaskData.duration) {
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