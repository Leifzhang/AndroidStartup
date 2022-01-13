package com.kronos.lib.startup

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */

interface StartupTaskGroup {
    fun group(builder: Startup.Builder): MutableList<StartupTask>
}

interface StartupTaskProcessGroup {
    fun group(builder: Startup.Builder, process: String): MutableList<StartupTask>
}

fun startupTaskGroup(lambda: MutableList<StartupTask>.() -> Unit): StartupTaskGroup {
    val startupTaskGroup = object : StartupTaskGroup {
        override fun group(builder: Startup.Builder): MutableList<StartupTask> {
            val list = mutableListOf<StartupTask>()
            lambda.invoke(list)
            return list
        }
    }
    return startupTaskGroup
}