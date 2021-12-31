package com.kronos.startup.dag.widget

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 *
 *  @Author LiABao
 *  @Since 2021/12/28
 *
 */

fun ViewGroup?.getRecyclerView(): RecyclerView? {
    if (this == null) {
        return null
    }
    return if (this is RecyclerView) {
        this
    } else {
        val parent = parent
        if (parent == null) {
            return null
        } else {
            (parent as ViewGroup?).getRecyclerView()
        }
    }
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