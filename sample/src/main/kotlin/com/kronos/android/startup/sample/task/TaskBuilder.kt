package com.kronos.android.startup.sample.task

import android.app.Application
import android.util.Log
import com.kronos.lib.startup.*
import com.kronos.lib.startup.group.StartupProcTaskGroupApplicationKsp
import com.kronos.lib.startup.group.StartupTaskGroupApplicationKsp
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
        mustAfterAnchor {
            MyAnchorTask()
        }
        addTask {
            asyncTask("asyncTaskA", {
                info("asyncTaskA")
            }, {
                dependOn("asyncTaskD")
            })
        }
        addAnchorTask {
            asyncTask("asyncTaskB", {
                info("asyncTaskB")
            }, {
                dependOn("asyncTaskA")
                await = true
            })
        }
        addAnchorTask {
            asyncTaskBuilder("asyncTaskC") {
                info("asyncTaskC")
                sleep(1000)
            }.apply {
                await = true
                dependOn("asyncTaskE")
            }.build()
        }
        addAnchorTask {
            asyncTask("asyncTaskD") {
                info("asyncTaskD")
                sleep(1000)
            }
        }
        addAnchorTask {
            asyncTask("asyncTaskE") {
                info("asyncTaskE")
                sleep(10000)
            }
        }
        addTaskGroup { taskGroup() }
        addTaskGroup { StartupTaskGroupApplicationKsp() }
        addProcTaskGroup { StartupProcTaskGroupApplicationKsp() }
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