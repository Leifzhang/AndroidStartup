package com.kronos.lib.startup.step

import android.content.Context
import com.kronos.lib.startup.StartupTaskProcessGroup

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/19
 *
 */
object StepStartupPack {

    private val stepTaskBuilders by lazy { mutableListOf<StepTaskBuilder>() }

    private val lifeCycleStep by lazy {
        hashMapOf<String, MutableList<StartupTaskProcessGroup>>()
    }

    fun onApplicationAttach(
        context: Context,
        builder: MutableList<StepTaskBuilder>.() -> Unit
    ) {
        builder.invoke(stepTaskBuilders)
    }

    fun dispatcher() {
        stepTaskBuilders.forEach {
            it.invoke().forEach {
                val key = it.lifecycle().value
                lifeCycleStep
            }
        }
    }
}