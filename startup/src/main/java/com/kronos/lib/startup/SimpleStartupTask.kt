package com.kronos.lib.startup

import com.kronos.startup.TaskNameProvider

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
abstract class SimpleStartupTask : StartupTask {

    internal val dependOns = hashSetOf<String>()

    override fun mainThread(): Boolean {
        return true
    }

    override fun await(): Boolean {
        return false
    }

    final override fun tag(): String {
        return taskNameProvider.name()
    }

    abstract val taskNameProvider: TaskNameProvider

    override fun onTaskStart() {

    }

    override fun onTaskCompleted() {

    }

    final override fun dependencies(): MutableList<String> {
        return dependOns.toMutableList()
    }

    private fun dependOn(name: String) {
        dependOns.add(name)
    }

    fun dependOn(provider: TaskNameProvider) {
        dependOn(provider.name())
    }
}

fun TaskNameProvider.name(): String {
    return javaClass.simpleName
}