package com.kronos.startup.dag.sql

import android.content.Context
import androidx.room.Room

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/10
 *
 */
object StartupDatabaseHelper {

    lateinit var databaseInstance: StartupDatabase


    @Synchronized
    fun init(context: Context): StartupDatabase {
        databaseInstance = Room.databaseBuilder(
            context,
            StartupDatabase::class.java,
            DATABASE_NAME
        ).build()
        return databaseInstance
    }

    private const val DATABASE_NAME = "startup_database.db"


}