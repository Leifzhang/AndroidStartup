package com.kronos.startup.sample.report

import android.content.Context
import android.util.Log
import com.kronos.lib.startup.NetworkSdkTaskProvider
import com.kronos.lib.startup.TaskRunner
import com.kronos.startup.annotation.Lifecycle
import com.kronos.startup.annotation.Process
import com.kronos.startup.annotation.Step
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
@DependOn(dependOn = [NetworkSdkTaskProvider::class])
@Startup(strategy = Process.MAIN)
@Step(Lifecycle.AttachApplication)
class ReportSdkTask : TaskRunner {



    override fun run(context: Context) {
        info("ReportSdkTask")
    }

}

fun info(info: String) {
    val threadName = Thread.currentThread().name
    Log.i(TAG, "[threadName:$threadName] $info")
}


const val TAG = "START-DSL—TEST"