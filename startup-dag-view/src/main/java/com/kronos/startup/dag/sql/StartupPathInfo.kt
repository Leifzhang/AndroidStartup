package com.kronos.startup.dag.sql

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


/**
 *
 *  @Author LiABao
 *  @Since 2021/12/10
 *
 */

@Entity(tableName = "privacy")
@TypeConverters(PathTypeConvert::class)
data class StartupPathInfo(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "date")
    var data: Long = 0L,
    @ColumnInfo(name = "process")
    var process: String? = null,
    @ColumnInfo(name = "dagPath")
    var dagPath: MutableList<StartupPathDataInfo>?,
    @ColumnInfo(name = "pathHashCode")
    var pathHashCode: Int = dagPath.hashCode()
) {
    fun updateHashCode() {
        pathHashCode = dagPath.hashCode()
    }
}


data class StartupPathDataInfo(
    val name: String?,
    val threadName: String?
)
