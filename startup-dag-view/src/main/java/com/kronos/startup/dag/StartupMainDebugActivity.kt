package com.kronos.startup.dag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
class StartupMainDebugActivity : AppCompatActivity(R.layout.startup_activity_main_debug) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<Button>(R.id.timeLineBtn).setOnClickListener {
            startupTimeLineActivity()
        }
        findViewById<Button>(R.id.startupPathBtn).setOnClickListener {
            startDagPathActivity()
        }
        findViewById<Button>(R.id.taskDurationBtn).setOnClickListener {
            startTaskDurationActivity()
        }

    }
}


fun Context.startDagMainActivity() {
    val intent = Intent(this, StartupMainDebugActivity::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}