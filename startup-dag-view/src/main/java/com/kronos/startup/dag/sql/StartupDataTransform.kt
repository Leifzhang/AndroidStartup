package com.kronos.startup.dag.sql

import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.lib.startup.utils.ProcessUtils

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/30
 *
 */

fun MutableList<StartupTaskData>.transform(data: Long): StartupPathInfo {
    val list = getPath()
    return StartupPathInfo(
        data = data,
        process = ProcessUtils.myProcName(),
        dagPath = list
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