package com.kronos.startup.sample.report

import android.content.Context
import android.util.Log
import com.kronos.lib.startup.NetworkSdkTaskProvider
import com.kronos.lib.startup.TaskRunner
import com.kronos.startup.annotation.startup.Async
import com.kronos.startup.annotation.startup.Await
import com.kronos.startup.annotation.startup.DependOn
import com.kronos.startup.annotation.startup.Startup
import com.kronos.startup.sample.report.di.ReportInitDelegate
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
@Async
@Await
@DependOn(dependOn = [NetworkSdkTaskProvider::class])
@Startup
class ReportSdkTask : KoinComponent, TaskRunner {

    private val initDelegate: ReportInitDelegate by inject()

    override fun run(context: Context) {
        info("ReportSdkTask appName is:${initDelegate.getAppName()}")
    }

}

fun info(info: String) {
    val threadName = Thread.currentThread().name
    Log.i(TAG, "[threadName:$threadName] $info")
}


const val TAG = "ReportSdkTask"