package com.kronos.lib.startup

import android.app.Application
import java.util.*

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */

internal const val TAG = "Startup"

class Startup private constructor(private val builder: Builder) {

    private val manager = StartupTaskManager()

    companion object {
        @JvmStatic
        fun newBuilder(): Builder {
            return Builder()
        }
    }


    class Builder {

        val tasks: MutableList<StartupTask> = ArrayList()
        var app: Application? = null
        private var mTaskAnchor: StartupTask? = null

        fun attach(app: Application): Builder {
            this.app = app
            return this
        }

        fun addTask(task: StartupTask): Builder {
            if (!tasks.contains(task)) {
                tasks.add(task)
            }
            return this
        }

        fun addTaskGroup(group: StartupTaskGroup): Builder {
            val taskList = group.group().takeIf { it.isNotEmpty() } ?: return this
            taskList.forEach {
                if (!tasks.contains(it)) {
                    tasks.add(it)
                }
            }
            return this
        }

        fun setTaskAnchor(taskAnchor: StartupTask): Builder {
            mTaskAnchor = taskAnchor
            return this
        }

        fun dependAnchorTask(task: StartupTask): Builder {
            if (!tasks.contains(task)) {
                tasks.add(AnchorTaskWrap(task, mTaskAnchor?.tag() ?: ""))
            }
            return this
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