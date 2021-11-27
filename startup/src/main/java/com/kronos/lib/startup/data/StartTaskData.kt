package com.kronos.lib.startup.data

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/27
 *
 */
data class StartTaskData(
    val taskName: String,
    val cost: Long,
    val message: String,
    val dependencies: MutableList<String>
)
