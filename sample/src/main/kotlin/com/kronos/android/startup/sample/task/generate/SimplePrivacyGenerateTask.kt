package com.kronos.android.startup.sample.task.generate

import android.content.Context
import com.kronos.android.startup.sample.task.info
import com.kronos.android.startup.sample.task.utils.toast
import com.kronos.lib.startup.TaskRunner
import com.kronos.startup.annotation.Lifecycle
import com.kronos.startup.annotation.Stage
import com.kronos.startup.annotation.startup.Startup

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/25
 *
 */
@Startup
@Stage(Lifecycle.AfterUserPrivacy)
class SimplePrivacyGenerateTask : TaskRunner {

    override fun run(context: Context) {
        info("SimplePrivacyGenerateTask")
        "完成用户隐私协议".toast(context)
    }

}