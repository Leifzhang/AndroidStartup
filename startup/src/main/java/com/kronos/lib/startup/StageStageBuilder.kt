package com.kronos.lib.startup

import com.kronos.startup.annotation.Lifecycle

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/24
 *
 */
interface StageStageBuilder {
    fun invoke(lifeCycle: Lifecycle, taskBuilder: TaskBuilder)
}