package com.kronos.android.startup.sample.task.generate

import android.content.Context
import com.kronos.android.startup.sample.AsyncTask1Provider
import com.kronos.android.startup.sample.SimpleTask2Provider
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.TaskRunner
import com.kronos.startup.annotation.Process
import com.kronos.startup.annotation.startup.Async
import com.kronos.startup.annotation.startup.Await
import com.kronos.startup.annotation.startup.DependOn
import com.kronos.startup.annotation.startup.Startup

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
@Async
@Await
@DependOn(
    dependOn = [AsyncTask1Provider::class, SimpleTask2Provider::class],
    dependOnTag = ["taskB"]
)
@Startup(strategy = Process.MAIN)
class SampleGenerate1Task : TaskRunner {

    override fun run(context: Context) {
        info("MyAnchorTask")
    }

}