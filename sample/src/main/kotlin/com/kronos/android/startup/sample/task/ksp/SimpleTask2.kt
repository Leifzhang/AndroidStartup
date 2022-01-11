package com.kronos.android.startup.sample.task.ksp

import android.content.Context
import com.kronos.android.startup.sample.SimpleTask2Provider
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.SimpleStartupTask
import com.kronos.lib.startup.TaskNameProvider
import com.kronos.startup.annotation.Process
import com.kronos.startup.annotation.StartupGroup

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/9
 *
 */
@StartupGroup(strategy = Process.MAIN)
class SimpleTask2 : SimpleStartupTask() {

    override val taskNameProvider: TaskNameProvider
        get() = SimpleTask2Provider

    override fun run(context: Context) {
        info("SimpleTask2")
    }

}
