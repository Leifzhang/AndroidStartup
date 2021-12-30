package com.kronos.startup.dag.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/13
 *
 */

fun getDateFormat(data: Date = Date()): Long {
    return format.format(data).toLongOrNull() ?: 0
}

val format by lazy { SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA) }
