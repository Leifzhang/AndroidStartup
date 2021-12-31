package com.kronos.startup.dag

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.kronos.lib.startup.StartupConfig
import com.kronos.startup.dag.sql.StartupDatabaseHelper
import com.kronos.startup.dag.sql.getPath
import com.kronos.startup.dag.sql.onTaskAdd
import com.kronos.startup.dag.sql.transform
import com.kronos.startup.dag.utils.getDateFormat
import com.kronos.startup.dag.utils.getVersionCode
import com.kronos.startup.dag.utils.postUI
import kotlin.concurrent.thread

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/29
 *
 */
class StartupDagInstallProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        context?.let { StartupDatabaseHelper.init(it) }
        versionCode = context.getVersionCode()
        StartupConfig.onStartupFinishedListener = {
            thread {
                val dao = StartupDatabaseHelper.databaseInstance.startupDao()
                var data = dao.getStartupPathInfo(formatKey)
                if (data == null) {
                    data = this.transform(formatKey)
                    dao.insertInfo(data)
                } else {
                    val path = getPath()
                    data.dagPath?.addAll(path)
                    data.updateHashCode()
                    dao.updateInfo(data)
                }
                postUI {
                    context?.startTaskDurationActivity()
                }
                this.onTaskAdd()
            }
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    companion object {
        val formatKey by lazy { getDateFormat() }
        var versionCode = 0L
    }
}