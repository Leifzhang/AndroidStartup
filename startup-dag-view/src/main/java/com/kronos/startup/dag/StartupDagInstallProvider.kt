package com.kronos.startup.dag

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.kronos.lib.startup.StartupConfig

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/29
 *
 */
class StartupDagInstallProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        StartupConfig.onStartupFinishedListener = {
            context?.startupDagActivity(this)
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
}