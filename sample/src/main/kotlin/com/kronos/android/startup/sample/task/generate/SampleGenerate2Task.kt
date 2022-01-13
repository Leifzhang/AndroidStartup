package com.kronos.android.startup.sample.task.generate

import android.content.Context
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.SampleGenerate1TaskProvider
import com.kronos.lib.startup.TaskRunner
import com.kronos.startup.annotation.Process
import com.kronos.startup.annotation.startup.DependOn
import com.kronos.startup.annotation.startup.MustAfter
import com.kronos.startup.annotation.startup.Startup

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/12
 *
 */
@DependOn(
    dependOn = [SampleGenerate1TaskProvider::class]
)
@Startup(strategy = Process.OTHER, processName = ["web"])
@MustAfter
class SampleGenerate2Task : TaskRunner {

    override fun run(context: Context) {
        info("SampleGenerate2Task")
    }

}