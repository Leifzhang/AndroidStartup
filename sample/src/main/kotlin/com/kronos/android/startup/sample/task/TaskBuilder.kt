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
fun Application.createStartup(): Startup.Builder = run {
    startUp(this) {
        addTask {
            simpleTask("taskA") {
                info("taskA")
            }
        }
        addTask {
            simpleTask("taskB") {
                info("taskB")
            }
        }
        addTask {
            simpleTask("taskC") {
                info("taskC")
            }
        }
        addTask {
            simpleTaskBuilder("taskD") {
                info("taskD")
            }.apply {
                dependOn("taskC")
            }.build()
        }
        buildAnchorTask {
            MyAnchorTask()
        }
        addTask {
            asyncTask("asyncTaskA", {
                info("asyncTaskA")
            }, {
                dependOn("asyncTaskD")
            })
        }
        mustAfterAnchor {
            asyncTask("asyncTaskB", {
                info("asyncTaskB")
            }, {
                dependOn("asyncTaskA")
                await = true
            })
        }
        mustAfterAnchor {
            asyncTaskBuilder("asyncTaskC") {
                info("asyncTaskC")
                sleep(1000)
            }.apply {
                await = true
                dependOn("asyncTaskE")
            }.build()
        }
        mustAfterAnchor {
            asyncTask("asyncTaskD") {
                info("asyncTaskD")
                sleep(1000)
            }
        }
        mustAfterAnchor {
            asyncTask("asyncTaskE") {
                info("asyncTaskE")
                sleep(10000)
            }
        }
        addTaskGroup { taskGroup() }
    }
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