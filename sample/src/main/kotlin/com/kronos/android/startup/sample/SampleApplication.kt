package com.kronos.android.startup.sample

import android.app.Application
import com.kronos.android.startup.sample.task.create
import com.kronos.lib.startup.StartupConfig

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        StartupConfig.debugMode = true
        create()
    }
}