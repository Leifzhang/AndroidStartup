package com.kronos.android.startup.sample.task

import android.app.Application
import android.util.Log
import com.kronos.lib.startup.addTask
import com.kronos.lib.startup.asyncTask
import com.kronos.lib.startup.startUp

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
        })
        dependAnchorTask(task)
    }.build().start()
}


fun info(info: String) {
    val threadName = Thread.currentThread().name
    Log.i(TAG, "[threadName:$threadName] $info")
}

const val TAG = "START-DSL—TEST"