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
class SimpleTask1 : SimpleStartupTask() {

    override fun run(context: Context) {
        info("AsyncTask1")
    }
}