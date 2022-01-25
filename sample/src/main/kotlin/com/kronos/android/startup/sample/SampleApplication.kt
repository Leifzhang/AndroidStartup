package com.kronos.android.startup.sample

import android.app.Application
import android.content.Context
import com.kronos.android.startup.sample.task.createStartup
import com.kronos.lib.startup.StartupConfig
import com.kronos.lib.startup.step.StepStartupPack
import com.kronos.lib.startup.step.ApplicationStageBuilder

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class SampleApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        StepStartupPack.onApplicationAttach(requireNotNull(base)) {
            add(ApplicationStageBuilder())
        }

    }

    override fun onCreate() {
        super.onCreate()
        StartupConfig.debugMode = true
        createStartup().build().start()
    }
}
