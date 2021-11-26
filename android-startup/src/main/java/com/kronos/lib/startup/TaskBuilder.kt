package com.kronos.lib.startup

import android.content.Context

/**
 *
 *  @Author LiABao
 *  @Since 2021/9/3
 *
 */
class TaskBuilder(val runnable: (Context) -> Unit = {}) {

    val dependOnTasks = mutableListOf<String>()
    var mainThread = true
    var await = true
    var tag: String? = null


    fun dependOn(task: String) {
        dependOnTasks.add(task)
    }

    fun build(): SimpleStartupTask {
        return SimpleBuilderTask(this)
    }

    override fun toString(): String {
        return "TaskBuilder(dependOnTasks=$dependOnTasks,  tag=$tag)"
    }


}


private class SimpleBuilderTask(private val builder: TaskBuilder) : SimpleStartupTask() {

    override fun run(context: Context) {
        builder.runnable.invoke(context)
    }

    override fun mainThread(): Boolean {
        return builder.mainThread
    }

    override fun await(): Boolean {
        return builder.await
    }

    override fun dependencies(): List<String> {
        return builder.dependOnTasks
    }

    override fun tag(): String {
        return builder.tag ?: ""
    }

    override fun toString(): String {
        return "SimpleBuilderTask(builder=$builder)"
    }

}