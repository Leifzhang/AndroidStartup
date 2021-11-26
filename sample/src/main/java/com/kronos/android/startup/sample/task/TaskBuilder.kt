package com.kronos.android.startup.sample.task

import android.app.Application
import android.util.Log
import com.kronos.lib.startup.*
import java.lang.Thread.sleep

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
fun Application.create() {
    startUp(this) {
        addTask("taskA") {
            info("taskA")
        }
        addTask({
            info("taskD")
        }, {
            tag = "taskD"
            dependOn("taskC")
        })
        addTask("taskB") {
            info("taskB")
        }
        addTask("taskC") {
            info("taskC")
        }
        val anchorTask = MyAnchorTask()
        addTask(anchorTask)
        setTaskAnchor(anchorTask)
        val task = asyncTask("asyncTaskA", {
            info("asyncTaskA")
        }, {
            dependOn("asyncTaskD")
        })
        dependAnchorTask(task)
        addTaskGroup(taskGroup())
        dependAnchorTask(asyncTask("asyncTaskB", {
            info("asyncTaskB")
        }, {
            dependOn("asyncTaskA")
        }))
        dependAnchorTask(asyncTask("asyncTaskC", {
            info("asyncTaskC")
            sleep(1000)
        }))
        dependAnchorTask(asyncTask("asyncTaskD", {
            info("asyncTaskD")
            sleep(1000)
        }))
        dependAnchorTask(asyncTask("asyncTaskE", {
            info("asyncTaskE")
            sleep(1000)
        }))
    }.build().start()
}

// 启动任务组
fun taskGroup(): StartupTaskGroup {
    return startupTaskGroup {
        add(simpleTask("group_1") {
            info("group_1")
        })
        add(simpleTask("group_2") {
            info("group_2")
        })
    }
}

fun info(info: String) {
    val threadName = Thread.currentThread().name
    Log.i(TAG, "[threadName:$threadName] $info")
}

const val TAG = "START-DSL—TEST"