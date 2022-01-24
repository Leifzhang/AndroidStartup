package com.kronos.android.startup.sample.task.ksp

import android.content.Context
import com.kronos.android.startup.sample.SimpleTask1Provider
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.SimpleStartupTask
import com.kronos.startup.TaskNameProvider
import com.kronos.startup.annotation.StartupGroup

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
@StartupGroup
class SimpleTask1 : SimpleStartupTask() {
    override val taskNameProvider: TaskNameProvider
        get() = SimpleTask1Provider

    override fun run(context: Context) {
        info("AsyncTask1")
    }

}