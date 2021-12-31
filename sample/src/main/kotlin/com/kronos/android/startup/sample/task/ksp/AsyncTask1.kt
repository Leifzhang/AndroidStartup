package com.kronos.android.startup.sample.task.ksp

import android.content.Context
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.SimpleStartupTask
import com.kronos.startup.annotation.StartupGroup

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
@StartupGroup(group = "ksp")
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

    override fun dependencies(): MutableList<String> {
        return super.dependencies().apply {

        }
    }
}