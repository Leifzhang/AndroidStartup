package com.kronos.startup.annotation

/**
 * @Author LiABao
 * @Since 2022/1/19
 */
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention
annotation class Step(val lifecycle: Lifecycle = Lifecycle.OnApplicationCrate)