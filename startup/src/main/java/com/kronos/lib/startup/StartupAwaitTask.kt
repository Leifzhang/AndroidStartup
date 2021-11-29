package com.kronos.lib.startup

import android.content.Context
import android.os.SystemClock
import com.kronos.lib.startup.logger.KLogger
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
        val timeUsage = SystemClock.elapsedRealtime()
        KLogger.i(TAG, "taskName:${task.tag()} start await")
        countDownLatch.await()
        KLogger.i(
            TAG,
            "taskName:${task.tag()}  await costa:${(SystemClock.elapsedRealtime() - timeUsage) / 1000} "
        )
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

    companion object {
        const val TAG = "StartupAwaitTask"
    }
}