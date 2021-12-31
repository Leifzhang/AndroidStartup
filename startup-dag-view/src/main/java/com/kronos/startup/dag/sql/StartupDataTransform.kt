package com.kronos.startup.dag.sql

import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.lib.startup.utils.ProcessUtils
import com.kronos.startup.dag.utils.getDayFormat

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/30
 *
 */

fun MutableList<StartupTaskData>.transform(data: Long): StartupPathInfo {
    val list = getPath()
    return StartupPathInfo(
        date = data,
        process = ProcessUtils.myProcName(),
        dagPath = list,
        pathHashCode = list.taskNameHash()
    )
}

fun MutableList<StartupTaskData>.getPath(): MutableList<StartupPathDataInfo> {
    val list = mutableListOf<StartupPathDataInfo>()
    forEach {
        val info = StartupPathDataInfo(it.taskName, it.threadName)
        list.add(info)
    }
    return list
}


fun MutableList<StartupTaskData>.onTaskAdd() {
    val dao = StartupDatabaseHelper.databaseInstance.startupDao()
    forEach {
        val task = dao.getTaskInfo(
            requireNotNull(it.taskName),
            getDayFormat(), ProcessUtils.myProcName() ?: ""
        )
        if (task == null) {
            val sql = it.toSql()
            dao.insertTaskInfo(sql)
        } else {
            task.plus(it.duration)
            dao.updateTaskInfo(task)
        }
    }
}


