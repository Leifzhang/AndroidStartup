package com.kronos.startup.dag.repo

import com.kronos.startup.dag.sql.StartTaskInfo
import com.kronos.startup.dag.sql.StartupDatabaseHelper
import com.kronos.startup.dag.utils.getValueByDefault
import com.kronos.startup.dag.utils.postUI
import kotlin.concurrent.thread

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
class TaskDurationRepo {

    private val dao = StartupDatabaseHelper.databaseInstance.startupDao()

    fun getTaskDurationList(invoke: (MutableList<StartTaskInfo>) -> Unit) {
        thread {
            val list = dao.getDayTask()
            val oldList = dao.getHistoryTask()
            val hashMap = linkedMapOf<String?, StartTaskInfo>()
            oldList.forEach {
                val task = hashMap.getValueByDefault(it.taskName) {
                    it
                }
                if (task != it) {
                    task.mix(it)
                }
            }
            val taskList = mutableListOf<StartTaskInfo>()
            list.forEach {
                taskList.add(it)
                hashMap[it.taskName]?.let { it1 ->
                    taskList.add(it1)
                    hashMap.remove(it.taskName)
                }
            }
            hashMap.forEach {
                taskList.add(it.value)
            }
            postUI {
                invoke.invoke(taskList)
            }
        }
    }

}