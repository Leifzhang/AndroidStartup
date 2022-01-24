package com.kronos.startup.annotation

/**
 *
 *  @Author LiABao
 *  @Since 2022/1/19
 *
 */
enum class Lifecycle(val value: String) {
    AttachApplication("AttachApplication"), OnApplicationCrate("OnApplicationCrate"),
    AfterUserPrivacy("AfterUserPrivacy")
}


fun String?.nameToLifeCycle(): Lifecycle {
    if (this == null) {
        return Lifecycle.OnApplicationCrate
    }
    var lastIndex = lastIndexOf(".") + 1
    if (lastIndex <= 0) {
        lastIndex = 0
    }
    val value = when (substring(lastIndex)) {
        "AttachApplication" -> {
            Lifecycle.AttachApplication
        }
        "OnApplicationCrate" -> {
            Lifecycle.OnApplicationCrate
        }
        "AfterUserPrivacy" -> {
            Lifecycle.AfterUserPrivacy
        }
        else -> {
            Lifecycle.OnApplicationCrate
        }
    }
    return value
}
