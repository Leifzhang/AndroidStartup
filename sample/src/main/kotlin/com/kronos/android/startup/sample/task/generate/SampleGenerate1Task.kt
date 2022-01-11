package com.kronos.android.startup.sample.task.generate

import android.content.Context
import com.kronos.android.startup.sample.task.info
import com.kronos.lib.startup.TaskRunner
import com.kronos.startup.annotation.startup.Async
import com.kronos.startup.annotation.startup.Await
import com.kronos.startup.annotation.startup.Startup

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
@Async
@Await
@Startup()
class SampleGenerate1Task : TaskRunner {

    override fun run(context: Context) {
        info("MyAnchorTask")
    }

}