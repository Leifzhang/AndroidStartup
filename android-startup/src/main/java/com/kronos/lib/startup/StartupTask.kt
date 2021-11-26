package com.kronos.lib.startup

import android.content.Context

/**
 * @author : windfall
 * @date : 2021/6/16
 * @mail : liuchangjiang@bilibili.com
 */
interface StartupTask {

    fun run(context: Context)

    fun mainThread(): Boolean

    fun await(): Boolean

    /* 依赖的tag*/
    fun dependencies(): List<String> = emptyList()

    fun tag(): String

    fun onTaskStart(): () -> Unit = {}

    fun onTaskCompleted(): () -> Unit = {}
}
