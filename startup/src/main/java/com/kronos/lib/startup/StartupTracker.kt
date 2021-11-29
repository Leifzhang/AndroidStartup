package com.kronos.lib.startup

import java.util.concurrent.ConcurrentHashMap

/**
 * @author : windfall
 * @date : 2021/6/17
 * @mail : liuchangjiang@bilibili.com
 */

private val reportMap: MutableMap<String, String> = ConcurrentHashMap()

internal fun track(taskName: String, duration: Long) {
    reportMap[taskName] = duration.toString()
}
