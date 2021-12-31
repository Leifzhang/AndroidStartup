/*
 * Copyright (c) 2015-2018 BiliBili Inc.
 */
package com.kronos.startup.dag.utils

import android.content.Context
import android.content.res.Resources
import android.os.Build
import kotlin.math.roundToInt

/**
 * @author liabao
 */
fun Int.dpToPx(): Int {
    return (getResource().displayMetrics.density * this).roundToInt()
}

fun Float.dpToPx(): Int {
    return (getResource().displayMetrics.density * this).roundToInt()
}

fun Double.dpToPx(): Int {
    return (getResource().displayMetrics.density * this).roundToInt()
}


private fun getResource(): Resources {
    return Resources.getSystem()
}


internal fun Context?.getVersionCode(): Long {
    val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        this?.packageManager?.getPackageInfo(this.packageName, 0)?.longVersionCode
    } else {
        this?.packageManager?.getPackageInfo(this.packageName, 0)?.versionCode?.toLong()
    } ?: 0L
    return versionCode
}
