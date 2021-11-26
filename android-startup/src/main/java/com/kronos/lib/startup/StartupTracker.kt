package com.kronos.lib.startup

import java.util.concurrent.ConcurrentHashMap

/**
 * @author : windfall
 * @date : 2021/6/17
 * @mail : liuchangjiang@bilibili.com
 */

private val reportMap: MutableMap<String, String> = ConcurrentHashMap()
internal fun track(task: StartupTask, duration: Long) {
    reportMap[getTag(task)] = duration.toString()
}

internal fun track(taskName: String, duration: Long) {
    reportMap[taskName] = duration.toString()
}


private fun getTag(task: StartupTask): String {
    return task.tag()?.takeIf { it.isNotEmpty() } ?: task.javaClass.simpleName
}