package com.kronos.lib.startup

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
abstract class SimpleStartupTask : StartupTask {

    private val dependOns = hashSetOf<String>()

    override fun mainThread(): Boolean {
        return true
    }

    override fun await(): Boolean {
        return false
    }

    override fun tag(): String {
        return javaClass.canonicalName ?: ""
    }

    override fun onTaskStart() {

    }

    override fun onTaskCompleted() {

    }

    override fun dependencies(): MutableList<String> {
        return dependOns.toMutableList()
    }

    fun dependOn(name: String) {
        dependOns.add(name)
    }
}