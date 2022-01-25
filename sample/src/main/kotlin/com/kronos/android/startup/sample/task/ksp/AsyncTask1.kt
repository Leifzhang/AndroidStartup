package com.kronos.android.startup.sample.task.ksp

import android.content.Context
import com.kronos.android.startup.sample.AsyncTask1Provider
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.SimpleStartupTask
import com.kronos.startup.TaskNameProvider
import com.kronos.startup.annotation.Lifecycle
import com.kronos.startup.annotation.Step
import com.kronos.startup.annotation.StartupGroup

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
@StartupGroup
@Step(Lifecycle.AfterUserPrivacy)
class AsyncTask1 : SimpleStartupTask() {

    override fun mainThread(): Boolean {
        return false
    }

    override fun await(): Boolean {
        return true
    }

    override fun run(context: Context) {
        info("AsyncTask1")
    }

    override val taskNameProvider: TaskNameProvider
        get() = AsyncTask1Provider
}