package com.kronos.lib.startup

import com.kronos.lib.startup.data.StartupTaskData

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/27
 *
 */
object StartupConfig {

    var debugMode = true

    var onStartupFinishedListener: MutableList<StartupTaskData>.() -> Unit = {}

}