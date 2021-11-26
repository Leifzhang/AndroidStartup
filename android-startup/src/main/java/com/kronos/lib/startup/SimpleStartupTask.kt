package com.kronos.lib.startup

/**
 * @author : windfall
 * @date : 2021/6/16
 * @mail : liuchangjiang@bilibili.com
 */
abstract class SimpleStartupTask : StartupTask {

    override fun mainThread(): Boolean {
        return true
    }

    override fun await(): Boolean {
        return false
    }


}