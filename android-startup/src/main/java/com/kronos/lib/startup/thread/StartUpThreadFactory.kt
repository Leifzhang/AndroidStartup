package com.kronos.lib.startup.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/26
 *
 */
class StartUpThreadFactory : ThreadFactory {

    private val atomicInteger by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { AtomicInteger() }

    override fun newThread(r: Runnable?): Thread {
        val thread = Thread(r)
        thread.name = "StartUpThreadFactory-${atomicInteger.getAndIncrement()}"
        return thread
    }
}