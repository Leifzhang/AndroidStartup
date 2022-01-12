package com.kronos.startup.annotation.startup

import com.kronos.startup.annotation.Process

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention
annotation class Startup(
    val strategy: Process = Process.ALL,
    val processName: Array<String> = []
)

