package com.kronos.lib.startup

import android.content.Context

/**
 *
 *  @Author LiABao
 *  @Since 2021/9/3
 *
 */
@StartUpDsl
class TaskBuilder(val runnable: (Context) -> Unit = {}) {

    val dependOnTasks = mutableListOf<String>()
    var mainThread = true
    var await = true
    var tag: String? = null


    fun dependOn(task: String) {
        dependOnTasks.add(task)
    }

    fun build(): StartupTask {
        return BuilderTask(this)
    }

    override fun toString(): String {
        return "TaskBuilder(dependOnTasks=$dependOnTasks,  tag=$tag)"
    }

}

@StartUpDsl
private class BuilderTask(private val builder: TaskBuilder) : StartupTask {

    override fun run(context: Context) {
        builder.runnable.invoke(context)
    }

    override fun mainThread(): Boolean {
        return builder.mainThread
    }

    override fun await(): Boolean {
        return builder.await
    }

    override fun dependencies(): MutableList<String> {
        return builder.dependOnTasks
    }

    override fun tag(): String {
        return builder.tag ?: tag()
    }

    override fun onTaskStart() {

    }

    override fun onTaskCompleted() {

    }

    override fun toString(): String {
        return "SimpleBuilderTask(builder=$builder)"
    }

}