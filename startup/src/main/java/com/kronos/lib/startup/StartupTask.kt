package com.kronos.lib.startup

import android.content.Context

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
interface StartupTask {

    fun run(context: Context)

    fun mainThread(): Boolean

    fun await(): Boolean

    /* 依赖的tag*/
    fun dependencies(): MutableList<String> = mutableListOf()

    fun tag(): String

    fun onTaskStart()

    fun onTaskCompleted()
}

fun StartupTask.taskMessage(): String {
    var message =
        "isMainThread:${mainThread()}  isAwait:${await()}  "
    if (this is StartupAwaitTask) {
        //   message += ""
    }
    return message
}