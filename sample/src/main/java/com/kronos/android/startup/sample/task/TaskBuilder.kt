package com.kronos.android.startup.sample.task

import android.app.Application
import android.util.Log
import com.kronos.lib.startup.insertTask
import com.kronos.lib.startup.startUp

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
fun Application.create() {
    startUp(this) {
        insertTask("taskA") {
            info("taskA")
        }
        insertTask("taskB") {
            info("taskB")
        }
        insertTask("taskC") {
            info("taskC")
        }
    }.build().start()
}


fun info(info: String) {
    Log.i(TAG, info)
}

const val TAG = "START-DSL—TEST"