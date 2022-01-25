package com.kronos.android.startup.sample.task

import com.kronos.lib.startup.StartupTaskProcessGroup
import com.kronos.lib.startup.group.ApplicationProc1
import com.kronos.lib.startup.group.ApplicationProc2
import com.kronos.lib.startup.step.StepTaskBuilder

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/25
 *
 */
class SimpleStageBuilder : StepTaskBuilder {

    override fun invoke(): MutableList<StartupTaskProcessGroup> {
        val list = mutableListOf<StartupTaskProcessGroup>()
        list.add(ApplicationProc1())
        list.add(ApplicationProc2())
        return list
    }

}