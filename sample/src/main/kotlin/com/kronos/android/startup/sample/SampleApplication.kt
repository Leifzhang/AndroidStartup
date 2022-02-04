package com.kronos.android.startup.sample

import android.app.Application
import android.content.Context
import com.kronos.android.startup.sample.task.createStartup
import com.kronos.lib.startup.StartupConfig
import com.kronos.lib.startup.startUp
import com.kronos.lib.startup.step.ApplicationStepBuilder
import com.kronos.lib.startup.step.NetworkSdkDemoStepBuilder
import com.kronos.lib.startup.step.ReportSdkDemoStepBuilder
import com.kronos.lib.startup.step.StepStartupPack
import com.kronos.startup.annotation.Lifecycle

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class SampleApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        StepStartupPack.onApplicationAttach {
            add(ApplicationStepBuilder())
            add(NetworkSdkDemoStepBuilder())
            add(ReportSdkDemoStepBuilder())
        }
        startUp(this) {
            StepStartupPack.getTaskGroup(Lifecycle.AttachApplication)?.forEach {
                addProcTaskGroup(it)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        StartupConfig.debugMode = true
        createStartup().apply {
            StepStartupPack.getTaskGroup(Lifecycle.OnApplicationCrate)?.forEach {
                addProcTaskGroup(it)
            }
        }.build().start()
    }
}
