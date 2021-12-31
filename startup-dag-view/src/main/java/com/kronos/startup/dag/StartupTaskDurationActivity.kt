package com.kronos.startup.dag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kronos.startup.dag.repo.TaskDurationRepo

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/31
 *
 */
class StartupTaskDurationActivity : AppCompatActivity(R.layout.startup_activity_task_duration) {
    private val repo by lazy {
        TaskDurationRepo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = findViewById<TextView>(R.id.today)
        repo.getTodayList {
            textView.text = it.toString()
        }
        repo.getHistoryList {
            Log.i(TAG, it.toString())
        }
    }

    companion object {
        const val TAG = "--StartupTaskActivity--"
    }

}

fun Context.startTaskDurationActivity() {
    val intent = Intent(this, StartupTaskDurationActivity::class.java)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}