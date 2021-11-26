package com.kronos.android.startup.sample.task

import android.content.Context
import com.kronos.lib.startup.SimpleStartupTask

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class MyAnchorTask : SimpleStartupTask() {

    override fun run(context: Context) {
        info("MyAnchorTask")
    }

    init {
        dependOn("taskB")
    }
}