package com.kronos.lib.startup

import android.content.Context
import java.util.concurrent.CountDownLatch

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/27
 *
 */
class StartupAwaitTask(val task: StartupTask) : SimpleStartupTask() {

    private var dependencies = task.dependencies()
    private lateinit var countDownLatch: CountDownLatch
    private lateinit var rightDependencies: List<String>

    override fun run(context: Context) {
        countDownLatch.await()
        task.run(context)
    }

    override fun dependencies(): MutableList<String> {
        return dependencies
    }

    fun allTaskTag(tags: HashSet<String>) {
        rightDependencies = dependencies.filter { tags.contains(it) }
        countDownLatch = CountDownLatch(rightDependencies.size)
    }

    fun dispatcher(taskName: String) {
        if (rightDependencies.contains(taskName)) {
            countDownLatch.countDown()
        }
    }

    override fun mainThread(): Boolean {
        return task.mainThread()
    }

    override fun await(): Boolean {
        return task.await()
    }

    override fun tag(): String {
        return task.tag()
    }

    override fun onTaskStart() {
        task.onTaskStart()
    }

    override fun onTaskCompleted() {
        task.onTaskCompleted()
    }
}