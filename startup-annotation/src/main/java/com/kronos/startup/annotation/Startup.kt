package com.kronos.startup.annotation


@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention
annotation class Startup(val group: String) {
}