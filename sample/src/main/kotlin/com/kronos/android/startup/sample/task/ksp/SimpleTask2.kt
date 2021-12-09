package com.kronos.android.startup.sample.task.ksp

import android.content.Context
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.SimpleStartupTask
import com.kronos.startup.annotation.Process
import com.kronos.startup.annotation.StartupGroup

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/9
 *
 */
@StartupGroup(group = "ksp", strategy = Process.MAIN)
class SimpleTask2 : SimpleStartupTask() {

    override fun run(context: Context) {
        info("SimpleTask2")
    }

}