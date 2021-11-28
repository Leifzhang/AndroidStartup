/*
 * Copyright (c) 2015-2018 BiliBili Inc.
 */
package com.kronos.startup.dag.utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
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

fun immersiveStatusBar(activity: Activity) {
    immersiveStatusBar(activity.window)
}

private fun immersiveStatusBar(window: Window) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        var systemUiVisibility = window.decorView.systemUiVisibility
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = systemUiVisibility
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
    val contentView = window.decorView.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
    val rootView = contentView.getChildAt(0)
    val statusBarHeight: Int = getStatusBarHeight(window.context)
    if (rootView != null) {
        val lp = rootView.layoutParams as FrameLayout.LayoutParams
        rootView.fitsSystemWindows = true
        lp.topMargin = -statusBarHeight
        rootView.layoutParams = lp
    }
}

/**
 * 获取状态栏高度
 */
private fun getStatusBarHeight(context: Context): Int {
    var result = 0
    val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        result = context.resources.getDimensionPixelSize(resId)
    }
    return result
}

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}