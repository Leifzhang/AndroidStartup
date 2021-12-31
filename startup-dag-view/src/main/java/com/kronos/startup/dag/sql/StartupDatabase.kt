package com.kronos.startup.dag.sql

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/10
 *
 */
@Database(entities = [StartupPathInfo::class, StartTaskInfo::class], version = 1)
@TypeConverters(PathTypeConvert::class)
abstract class StartupDatabase : RoomDatabase() {

    abstract fun startupDao(): StartupDao

}