package com.kronos.lib.startup

import android.content.Context
import android.os.SystemClock
import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.lib.startup.logger.KLogger
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set


internal class StartupTaskManager(executor: Executor? = null) {

    private val dispatcher = StartupDispatcher(executor, this)
    private var countDownLatch: CountDownLatch? = null
    private val taskList = mutableListOf<StartupTaskData>()

    fun start(context: Context, tasks: List<StartupTask>) {
        val result = sort(tasks)
        val awaitCount = result.filter { it.await() }.size
        if (awaitCount > 0) {
            countDownLatch = CountDownLatch(awaitCount)
            KLogger.i(TAG, "need await count: $awaitCount")
        }
        for (task in result) {
            val taskData = StartupTaskData(task.tag(), task.taskMessage(), task.dependencies())
            taskList.add(taskData)
            dispatcher.dispatch(context, task) {
                if (it.await()) {
                    countDownLatch?.countDown()
                }
                result.forEach { task ->
                    if (task is StartupAwaitTask) {
                        task.dispatcher(it.tag())
                    }
                }
                taskData.taskFinish()
                KLogger.i(
                    StartupDispatcher.COAST_TAG,
                    "${taskData.taskName}: task completed. cost: ${taskData.duration}ms"
                )
            }
        }
        try {
            val start = SystemClock.elapsedRealtime()
            countDownLatch?.await(2000, TimeUnit.MILLISECONDS)
            val duration = SystemClock.elapsedRealtime() - start
            KLogger.i(TAG, "await cost: ${duration}ms")
            StartupConfig.onStartupFinishedListener.invoke(taskList)
        } catch (e: Throwable) {
            KLogger.e(TAG, e.toString())
        }
    }




    /**
     * 拓扑排序
     */
    private fun sort(tasks: List<StartupTask>): List<StartupTask> {
        KLogger.i(TAG, "origin tasks:")
        printTasks(tasks)
        val result: MutableList<StartupTask> = ArrayList()
        val taskTags = hashSetOf<String>()

        val taskMap: MutableMap<String, StartupTask> = HashMap()
        val inDegreeMap: MutableMap<String, Int> = HashMap()
        val zeroInDegreeQueue = ArrayDeque<String>()
        val dependencyMap: MutableMap<String, MutableList<String>> = HashMap()
        tasks.forEach {
            taskTags.add(it.tag())
        }
        tasks.forEach { task ->
            val key = task.tag()
            if (task is StartupAwaitTask) {
                task.allTaskTag(taskTags)
            }
            taskMap[key] = task
            val dependencies = task.dependencies().filter {
                val contains = taskTags.contains(it)
                if (!contains) {
                    KLogger.w(TAG, "this  task :$it is illegal in dependencies")
                }
                contains
            }
            val inDegree = dependencies.size
            inDegreeMap[key] = inDegree
            if (inDegree == 0) {
                zeroInDegreeQueue.offer(key)
            } else {
                dependencies.forEach { dependency ->
                    if (dependencyMap[dependency] == null) {
                        dependencyMap[dependency] = arrayListOf()
                    }
                    if (taskTags.contains(key)) {
                        dependencyMap[dependency]?.add(key)
                    }
                }
            }
        }

        if (zeroInDegreeQueue.isEmpty()) {
            throw IllegalArgumentException("Start up dependencies must be cycle.")
        }

        while (!zeroInDegreeQueue.isEmpty()) {
            zeroInDegreeQueue.poll()?.let {
                taskMap[it]?.let { task ->
                    result.add(task)
                }
                dependencyMap[it]?.forEach { dependencyKey ->
                    val inDegree =
                        inDegreeMap[dependencyKey]?.minus(1)?.takeIf { inDegree -> inDegree >= 0 }
                            ?: 0
                    inDegreeMap[dependencyKey] = inDegree
                    if (inDegree == 0) {
                        zeroInDegreeQueue.offer(dependencyKey)
                    }
                }
            }
        }

        if (result.size != tasks.size) {
            throw IllegalArgumentException("Start up dependencies must be cycle or leak.")
        }
        KLogger.i(TAG, "sorted tasks: ")
        printTasks(result)
        return result
    }
}

private fun printTasks(tasks: List<StartupTask>) {
    val msg = StringBuilder()
    tasks.forEach {
        msg.append(it.string())
            .append(" | ")
    }
    KLogger.i(TAG, msg.toString())
}

fun StartupTask.string(): String {
    val tag = tag().takeIf { it.isNotBlank() } ?: javaClass.simpleName
    return "tag: $tag, mainThread: ${mainThread()}, await: ${await()}, dependencyCount: ${dependencies().size ?: 0}"
}