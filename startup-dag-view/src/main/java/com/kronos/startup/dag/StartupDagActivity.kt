package com.kronos.startup.dag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kronos.lib.startup.data.StartupTaskData

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/28
 *
 */
class StartupDagActivity : AppCompatActivity(R.layout.startup_activity_dag) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getParcelableArrayListExtra<StartupTaskData>(STARTUP_DATA)
    }
}


fun Context.startupDagActivity(data: MutableList<StartupTaskData>) {
    val intent = Intent(this, StartupDagActivity::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    intent.putParcelableArrayListExtra(STARTUP_DATA, data.toArrayList())
    startActivity(intent)
}

fun <T> MutableList<T>.toArrayList(): ArrayList<T> {
    return this as ArrayList
}

const val STARTUP_DATA = "STARTUP_DATA"