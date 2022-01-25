package com.kronos.lib.startup.utils

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/11
 *
 */
fun <K, V> MutableMap<K, V>.getValueByDefault(key: K, function: () -> V): V {
    if (!containsKey(key)) {
        this[key] = function.invoke()
    }
    return requireNotNull(this[key])
}