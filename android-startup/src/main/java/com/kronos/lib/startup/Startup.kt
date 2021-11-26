package com.kronos.lib.startup

import android.app.Application
import java.util.*

/**
 * @author : windfall
 * @date : 2021/6/16
 * @mail : liuchangjiang@bilibili.com
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
            val taskList = group.group()?.takeIf { it.isNotEmpty() } ?: return this
            taskList.forEach {
                if (!tasks.contains(it)) {
                    tasks.add(it)
                }
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