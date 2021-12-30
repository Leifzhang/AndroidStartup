package com.kronos.startup.dag.sql

import androidx.room.*
import com.kronos.lib.startup.utils.ProcessUtils


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

    @Query("SELECT * FROM privacy WHERE date=:date and process=:process")
    fun getStartupPathInfo(
        date: Long,
        process: String = ProcessUtils.myProcName() ?: ""
    ): StartupPathInfo?

    @Query("SELECT * FROM privacy WHERE process=:process and pathHashCode!=:pathHashCode order by id DESC LIMIT 1")
    fun selectNotSame(
        pathHashCode: Int,
        process: String = ProcessUtils.myProcName() ?: ""
    ): StartupPathInfo?


/*    @Query("SELECT * FROM privacy WHERE  date<=:date")
    fun getAllExpiredInfo(date: Long): List<StartupPathInfo>*/
}