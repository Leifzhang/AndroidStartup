package com.kronos.android.startup.sample.task.utils

import android.content.Context
import android.widget.Toast

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/25
 *
 */

fun String.toast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}