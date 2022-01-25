package com.kronos.lib.startup.step

import com.kronos.lib.startup.StartupTaskProcessGroup

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/24
 *
 */
interface StepTaskBuilder {
    fun invoke(): MutableList<StartupTaskProcessGroup>
}