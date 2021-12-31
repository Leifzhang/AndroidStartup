package com.kronos.android.startup.sample.task

import android.content.Context
import com.kronos.android.startup.sample.AsyncTask1Provider
import com.kronos.android.startup.sample.MyAnchorTaskProvider
import com.kronos.lib.startup.SimpleStartupTask
import com.kronos.lib.startup.TaskNameProvider

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class MyAnchorTask : SimpleStartupTask() {
    override val taskNameProvider: TaskNameProvider
        get() = MyAnchorTaskProvider

    override fun run(context: Context) {
        info("MyAnchorTask")
    }

    init {
        dependOn(AsyncTask1Provider)
    }
}