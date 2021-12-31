package com.kronos.startup.dag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kronos.startup.dag.adapter.TaskViewAdapter
import com.kronos.startup.dag.repo.TaskDurationRepo
import com.kronos.startup.dag.utils.dpToPx

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
        val taskRecyclerView = findViewById<RecyclerView>(R.id.taskRecyclerView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        taskRecyclerView.addItemDecoration(DividerItemDecoration().apply {
            setColorDrawable(Color.parseColor("#1482f0"), 1.dpToPx())
        })
        repo.getTaskDurationList {
            taskRecyclerView.adapter = TaskViewAdapter(it)
        }
        /* repo.getTodayList {
             textView.text = it.toString()
         }
         repo.getHistoryList {
             Log.i(TAG, it.toString())
         }*/
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