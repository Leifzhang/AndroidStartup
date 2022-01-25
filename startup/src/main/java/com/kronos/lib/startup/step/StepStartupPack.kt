package com.kronos.lib.startup.step

import com.kronos.lib.startup.StartupTaskProcessGroup
import com.kronos.lib.startup.utils.getValueByDefault
import com.kronos.startup.annotation.Lifecycle

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
        builder: MutableList<StepTaskBuilder>.() -> Unit
    ) {
        builder.invoke(stepTaskBuilders)
        dispatcher()
    }

    private fun dispatcher() {
        stepTaskBuilders.forEach {
            it.invoke().forEach { group ->
                val key = group.lifecycle().value
                val value = lifeCycleStep.getValueByDefault(key) {
                    mutableListOf()
                }
                value.add(group)
            }
        }
    }

    fun getTaskGroup(lifecycle: Lifecycle): MutableList<StartupTaskProcessGroup>? {
        return lifeCycleStep[lifecycle.value]
    }
}