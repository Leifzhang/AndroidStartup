package com.kronos.lib.startup

import android.app.Application
import android.content.Context

/**
 *
 *  @Author LiABao
 *  @Since 2021/9/3
 *
 */


@StartUpDsl
fun startUp(context: Application, invoke: Startup.Builder.() -> Unit): Startup.Builder =
    Startup.newBuilder(context).apply(invoke)

@StartUpDsl
fun Startup.Builder.addMainProcTaskGroup(builder: () -> StartupTaskGroup) =
    run { addMainProcTaskGroup(builder.invoke()) }

@StartUpDsl
fun Startup.Builder.addTaskGroup(builder: () -> StartupTaskGroup) =
    run { addTaskGroup(builder.invoke()) }

@StartUpDsl
fun Startup.Builder.addProcTaskGroup(builder: () -> StartupTaskProcessGroup) =
    run { addProcTaskGroup(builder.invoke()) }

@StartUpDsl
fun Startup.Builder.addTask(builder: () -> StartupTask) =
    run { addTask(builder.invoke()) }

@StartUpDsl
fun Startup.Builder.buildAnchorTask(builder: () -> StartupTask) =
    kotlin.run {
        val task = builder.invoke()
        addTask(task)
        addTaskAnchor(task)
    }

@StartUpDsl
fun Startup.Builder.mustAfterAnchor(builder: () -> StartupTask) =
    run { mustAfterAnchorTask(builder.invoke()) }


@StartUpDsl
fun Startup.Builder.addTask(name: String, runnable: (Context) -> Unit) =
    run { addTask(simpleTask(name, runnable)) }

@StartUpDsl
fun simpleTask(name: String, runnable: (Context) -> Unit) =
    TaskBuilder(runnable).apply {
        tag = name
    }.build()

@StartUpDsl
fun simpleTaskBuilder(name: String, runnable: (Context) -> Unit): TaskBuilder =
    TaskBuilder(runnable).apply {
        tag = name
    }

@StartUpDsl
fun simpleTask(name: String, runnable: (Context) -> Unit, builder: TaskBuilder.() -> Unit = {}) =
    TaskBuilder(runnable).apply {
        tag = name
        builder.invoke(this)
    }.build()


@StartUpDsl
fun asyncTaskBuilder(name: String, runnable: (Context) -> Unit): TaskBuilder =
    TaskBuilder(runnable).apply {
        tag = name
        mainThread = false
    }


@StartUpDsl
fun asyncTask(name: String, runnable: (Context) -> Unit) =
    TaskBuilder(runnable).apply {
        tag = name
        mainThread = false
    }.build()

@StartUpDsl
fun asyncTask(name: String, runnable: (Context) -> Unit, builder: TaskBuilder.() -> Unit = {}) =
    TaskBuilder(runnable).apply {
        tag = name
        mainThread = false
        builder.invoke(this)
    }.build()

@StartUpDsl
fun task(runnable: (Context) -> Unit, builder: TaskBuilder.() -> Unit = {}) =
    TaskBuilder(runnable).apply(builder).build()

@DslMarker
annotation class StartUpDsl