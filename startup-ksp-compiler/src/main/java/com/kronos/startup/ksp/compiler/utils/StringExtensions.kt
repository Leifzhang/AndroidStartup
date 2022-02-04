package com.kronos.startup.ksp.compiler.utils

/**
 *
 *  @Author LiABao
 *  @Since 2022/2/4
 *
 */
fun String.fixClassName(): String {
    val nameBuilder = StringBuilder()
    val replaceText = replace("[^0-9a-zA-Z_]+", "")
    var next = -1
    for (index in replaceText.indices) {
        val key = replaceText[index]
        if (index == next) {
            nameBuilder.append(replaceText[index].uppercaseChar())

        } else {
            if (replaceText[index] != '-') {
                nameBuilder.append(key)
            } else {
                next = index + 1
            }
        }
    }
    return nameBuilder.toString()
}
