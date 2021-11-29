package com.kronos.lib.startup

import android.app.Application
import java.util.*
import java.util.concurrent.ExecutorService

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */

internal const val TAG = "--Startup--"

class Startup private constructor(private val builder: Builder) {

    private val manager = StartupTaskManager(builder.mExecutor)


    companion object {
        @JvmStatic
        fun newBuilder(): Builder {
            return Builder()
        }
    }

    @StartUpDsl
    class Builder {

        val tasks: MutableList<StartupTask> = ArrayList()
        var app: Application? = null
        private var mTaskAnchor: StartupTask? = null
        internal var mExecutor: ExecutorService? = null


        fun attach(app: Application): Builder {
            this.app = app
            return this
        }

        fun addTask(task: StartupTask): Builder {
            addStartTask(task)
            return this
        }

        fun addTaskGroup(group: StartupTaskGroup): Builder {
            val taskList = group.group().takeIf { it.isNotEmpty() } ?: return this
            taskList.forEach {
                addStartTask(it)
            }
            return this
        }

        fun setTaskAnchor(taskAnchor: StartupTask): Builder {
            mTaskAnchor = taskAnchor
            return this
        }

        fun dependAnchorTask(task: StartupTask): Builder {
            addStartTask(AnchorTaskWrap(task, mTaskAnchor?.tag() ?: ""))
            return this
        }

        fun setExecutor(executor: ExecutorService): Builder {
            mExecutor = executor
            return this
        }

        private fun addStartTask(task: StartupTask) {
            if (!tasks.contains(task)) {
                if (!task.mainThread() && task.dependencies().size > 0) {
                    tasks.add(StartupAwaitTask(task))
                } else {
                    tasks.add(task)
                }
            }
        }

        fun build(): Startup {
            return Startup(this)
        }
    }

    fun start() {
        if (builder.app == null) {
            return
        }
        manager.start(builder.app!!.applicationContext, builder.tasks)
    }

}