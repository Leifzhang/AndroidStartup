package com.kronos.startup.dag.utils

import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.startup.dag.data.ThreadTaskData
import java.util.concurrent.CopyOnWriteArrayList

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/23
 *
 */


fun MutableList<StartupTaskData>.classify(): MutableList<ThreadTaskData> {
    val taskDataList = mutableListOf<ThreadTaskData>()
    this.looper(taskDataList)
    return taskDataList
}

fun List<StartupTaskData>.looper(
    taskDataList: MutableList<ThreadTaskData>
) {
    val firstName = first().threadName ?: ""
    val childThreads = classifyByTaskName(taskDataList, firstName)
    if (childThreads.isNotEmpty()) {
        childThreads.looper(taskDataList)
    }
}

fun List<StartupTaskData>.classifyByTaskName(
    taskDataList: MutableList<ThreadTaskData>,
    name: String,
): CopyOnWriteArrayList<StartupTaskData> {
    var lastThreadTaskData: ThreadTaskData? = null
    val childThreads = CopyOnWriteArrayList<StartupTaskData>()
    forEach {
        if (it.threadName.equals(name)) {
            lastThreadTaskData = ThreadTaskData(it)
            taskDataList.add(lastThreadTaskData!!)
        } else {
            childThreads.add(it)
            lastThreadTaskData?.bindMainTask(it)
        }
    }
    taskDataList.forEach {
        it.isTaskEnd(childThreads)
    }
    return childThreads
}


fun <K, V> MutableMap<K, V>.getValueByDefault(key: K, function: () -> V): V {
    if (!containsKey(key)) {
        this[key] = function.invoke()
    }
    return requireNotNull(this[key])
}