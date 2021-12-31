package com.kronos.startup.dag.sql

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.lib.startup.utils.ProcessUtils
import com.kronos.startup.dag.StartupDagInstallProvider
import com.kronos.startup.dag.utils.getDayFormat

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/30
 *
 */
@Entity(tableName = "startup_task")
data class StartTaskInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "taskName")
    val taskName: String?,
    @ColumnInfo(name = "duration")
    var duration: Long,
    @ColumnInfo(name = "threadName")
    val threadName: String?,
    @ColumnInfo(name = "count")
    var count: Int = 1,
    @ColumnInfo(name = "date")
    var date: Long = 0L,
    @ColumnInfo(name = "process")
    var process: String?,
    @ColumnInfo(name = "appVersion")
    var appVersion: Long = StartupDagInstallProvider.versionCode
) {

    fun plus(duration: Long) {
        count++
        this.duration += duration
    }

    fun mix(info: StartTaskInfo) {
        count += info.count
        this.duration += info.duration
    }

    fun average(): Long {
        return duration / count
    }
}


fun StartupTaskData.toSql(): StartTaskInfo {
    return StartTaskInfo(
        taskName = taskName,
        duration = duration,
        threadName = threadName,
        date = getDayFormat(),
        process = ProcessUtils.myProcName()
    )
}
