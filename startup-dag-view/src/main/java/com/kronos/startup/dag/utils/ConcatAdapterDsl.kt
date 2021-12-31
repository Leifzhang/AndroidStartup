package com.kronos.startup.dag.utils

import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 *
 *  @Author LiABao
 *  @Since 2021/5/11
 *
 */
inline fun builderConcatAdapter(invoke: MutableList<RecyclerView.Adapter<out RecyclerView.ViewHolder>>.() -> Unit):
        ConcatAdapter {
    val list = mutableListOf<RecyclerView.Adapter<out RecyclerView.ViewHolder>>()
    invoke.invoke(list)
    return ConcatAdapter(list)
}

inline fun MutableList<RecyclerView.Adapter<out RecyclerView.ViewHolder>>.adapter(
    invoke: () -> RecyclerView.Adapter<out RecyclerView.ViewHolder>
) = apply {
    add(invoke.invoke())
}



