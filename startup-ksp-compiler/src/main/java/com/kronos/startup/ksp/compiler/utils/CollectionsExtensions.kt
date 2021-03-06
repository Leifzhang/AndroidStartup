package com.kronos.startup.ksp.compiler.utils

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/11
 *
 */
internal fun <K, V> MutableMap<K, V>.getValueByDefault(key: K, function: () -> V): V {
    if (!containsKey(key)) {
        this[key] = function.invoke()
    }
    return requireNotNull(this[key])
}