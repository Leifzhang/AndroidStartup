package com.kronos.lib.startup

import android.app.Application
import android.content.Context

/**
 *
 *  @Author LiABao
 *  @Since 2021/9/3
 *
 */


fun startUp(context: Application, invoke: Startup.Builder.() -> Unit): Startup.Builder =
    Startup.newBuilder().attach(context).apply(invoke)


fun Startup.Builder.addTask(name: String, runnable: (Context) -> Unit) =
    run { addTask(simpleTask(name, runnable)) }


fun Startup.Builder.addTask(runnable: (Context) -> Unit, builder: TaskBuilder.() -> Unit) =
    run { addTask(task(runnable, builder)) }



fun Startup.Builder.addAsyncTask(
    name: String,
    runnable: (Context) -> Unit,
    builder: TaskBuilder.() -> Unit = {}
) = run {
    addTask(asyncTask(name, runnable, builder))
}

fun simpleTask(name: String, runnable: (Context) -> Unit) =
    TaskBuilder(runnable).apply { tag = name }.build()


fun asyncTask(name: String, runnable: (Context) -> Unit, builder: TaskBuilder.() -> Unit = {}) =
    TaskBuilder(runnable).apply {
        tag = name
        mainThread = false
        builder.invoke(this)
    }.build()

fun simpleTask(runnable: (Context) -> Unit) =
    TaskBuilder(runnable).build()

fun task(runnable: (Context) -> Unit, builder: TaskBuilder.() -> Unit = {}) =
    TaskBuilder(runnable).apply(builder).build()
