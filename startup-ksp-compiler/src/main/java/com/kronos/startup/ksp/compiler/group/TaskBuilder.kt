package com.kronos.startup.ksp.compiler.group

import com.squareup.kotlinpoet.ClassName

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/13
 *
 */
class TaskBuilder(
    val className: ClassName,
    val mustAfter: Boolean = false,
    val processes: ArrayList<String> = arrayListOf()
)
