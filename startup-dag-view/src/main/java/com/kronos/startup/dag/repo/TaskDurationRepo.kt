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

    fun getTodayList(invoke: (MutableList<StartTaskInfo>) -> Unit) {
        thread {
            val list = dao.getDayTask()
            postUI {
                invoke.invoke(list)
            }
        }
    }


    fun getHistoryList(invoke: (MutableList<StartTaskInfo>) -> Unit) {
        thread {
            val list = dao.getHistoryTask()
            val hashMap = linkedMapOf<String, StartTaskInfo>()
            list.forEach {
                val task = hashMap.getValueByDefault(it.taskName ?: "") {
                    it
                }
                if (task != it) {
                    task.mix(it)
                }
            }
            val historyList = mutableListOf<StartTaskInfo>()
            hashMap.forEach {
                historyList.add(it.value)
            }
            postUI {
                invoke.invoke(historyList)
            }
        }
    }
}