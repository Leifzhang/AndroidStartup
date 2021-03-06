package com.kronos.lib.startup.generate

import com.kronos.lib.startup.SimpleStartupTask

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/11
 *
 */
abstract class GenerateStartupTask : SimpleStartupTask() {

    init {
        dependOns.apply {
            addDependencies()
        }
    }

    open fun addDependencies() {

    }

    fun dependOnName(name: String) {
        dependOns.add(name)
    }
}