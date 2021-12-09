package com.kronos.lib.startup

import android.app.Application
import com.kronos.lib.startup.utils.ProcessUtils
import com.kronos.lib.startup.utils.isMainProc
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
        fun newBuilder(app: Application): Builder {
            return Builder(app)
        }
    }

    @StartUpDsl
    class Builder(val app: Application) {

        val tasks: MutableList<StartupTask> = ArrayList()
        private val processName by lazy {
            ProcessUtils.myProcName()
        }

        private val anchorTasks = mutableListOf<StartupTask>()
        internal var mExecutor: ExecutorService? = null

        init {
            ProcessUtils.application = app
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

        fun addMainProcTaskGroup(group: StartupTaskGroup): Builder {
            if (app.isMainProc(processName)) {
                val taskList = group.group().takeIf { it.isNotEmpty() } ?: return this
                taskList.forEach {
                    addStartTask(it)
                }
            }
            return this
        }


        fun addProcTaskGroup(group: StartupTaskProcessGroup): Builder {
            val taskList = group.group(processName ?: "").takeIf { it.isNotEmpty() } ?: return this
            taskList.forEach {
                addStartTask(it)
            }
            return this
        }

        fun addTaskAnchor(taskAnchor: StartupTask): Builder {
            anchorTasks.add(taskAnchor)
            return this
        }

        fun mustAfterAnchorTask(task: StartupTask): Builder {
            addStartTask(AnchorTaskWrap(task, anchorTasks))
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
        manager.start(builder.app.applicationContext, builder.tasks)
    }

}