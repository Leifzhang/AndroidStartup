package com.kronos.startup.annotation.startup

import com.kronos.startup.TaskNameProvider
import kotlin.reflect.KClass

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/12
 *
 */

@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention
annotation class DependOn(
    val dependOn: Array<KClass<out TaskNameProvider>>,
    val dependOnTag: Array<String> = []
)