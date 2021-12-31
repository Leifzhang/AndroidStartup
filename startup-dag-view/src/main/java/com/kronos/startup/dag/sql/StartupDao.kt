package com.kronos.startup.dag.sql

import androidx.room.*
import com.kronos.lib.startup.utils.ProcessUtils
import com.kronos.startup.dag.utils.getDayFormat


/**
 *
 *  @Author LiABao
 *  @Since 2021/12/10
 *
 */
@Dao
interface StartupDao {
    @Insert
    fun insertInfo(info: StartupPathInfo)

    @Delete
    fun deleteInfo(info: List<StartupPathInfo>)

    @Update
    fun updateInfo(info: StartupPathInfo)

    @Query("SELECT * FROM startup_path WHERE date=:date and process=:process")
    fun getStartupPathInfo(
        date: Long,
        process: String = ProcessUtils.myProcName() ?: ""
    ): StartupPathInfo?

    @Query("SELECT * FROM startup_path WHERE process=:process and pathHashCode!=:pathHashCode order by id DESC LIMIT 1")
    fun selectNotSame(
        pathHashCode: Int,
        process: String = ProcessUtils.myProcName() ?: ""
    ): StartupPathInfo?


    @Insert
    fun insertTaskInfo(info: StartTaskInfo)

    @Update
    fun updateTaskInfo(info: StartTaskInfo)

    @Query("SELECT * FROM startup_task WHERE taskName=:taskName and date=:date and process=:process")
    fun getTaskInfo(
        taskName: String,
        date: Long,
        process: String = ProcessUtils.myProcName() ?: ""
    ): StartTaskInfo?

    @Query("SELECT * FROM startup_task WHERE process=:process and date=:date ")
    fun getDayTask(
        date: Long = getDayFormat(),
        process: String = ProcessUtils.myProcName() ?: ""
    ): MutableList<StartTaskInfo>


    @Query("SELECT * FROM startup_task WHERE process=:process and date!=:date ")
    fun getHistoryTask(
        date: Long = getDayFormat(),
        process: String = ProcessUtils.myProcName() ?: ""
    ): MutableList<StartTaskInfo>

/*    @Query("SELECT * FROM privacy WHERE  date<=:date")
    fun getAllExpiredInfo(date: Long): List<StartupPathInfo>*/
}