package com.kronos.lib.startup

import android.content.Context

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/19
 *
 */
object StageStartupPack {

    private val map by lazy { hashMapOf<String, Startup.Builder>() }

    fun onApplicationAttach(
        context: Context,
        builder: (Context, HashMap<String, Startup.Builder>) -> Unit
    ) {
        builder.invoke(context, map)
    }
}