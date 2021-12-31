package com.kronos.startup.dag.sql

import android.text.TextUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kronos.startup.dag.StartupDagInstallProvider


/**
 *
 *  @Author LiABao
 *  @Since 2021/12/10
 *
 */

@Entity(tableName = "startup_path")
@TypeConverters(PathTypeConvert::class)
data class StartupPathInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "date")
    var date: Long = 0L,
    @ColumnInfo(name = "process")
    var process: String? = null,
    @ColumnInfo(name = "dagPath")
    var dagPath: MutableList<StartupPathDataInfo>?,
    @ColumnInfo(name = "pathHashCode")
    var pathHashCode: Int = dagPath.taskNameHash(),
    @ColumnInfo(name = "appVersion")
    var appVersion: Long = StartupDagInstallProvider.versionCode
) {
    fun updateHashCode() {
        pathHashCode = dagPath.taskNameHash()
    }
}


data class StartupPathDataInfo(
    val name: String?,
    val threadName: String?,
) {

    val mainThread: Boolean = threadName.isMainThread()
}


fun String?.isMainThread(): Boolean {
    return TextUtils.equals(this, "main")
}

fun MutableList<StartupPathDataInfo>?.taskNameHash(): Int {
    val taskName = StringBuilder()
    this?.forEach {
        taskName.append("${it.name}_${it.mainThread}")
    }
    return taskName.hashCode()
}