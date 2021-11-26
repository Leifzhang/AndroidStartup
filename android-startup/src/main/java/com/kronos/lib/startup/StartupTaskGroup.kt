package com.kronos.lib.startup

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */

interface StartupTaskGroup {

    fun group(): MutableList<StartupTask>
}