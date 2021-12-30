package com.kronos.startup.dag

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.startup.dag.adapter.HeaderAdapter
import com.kronos.startup.dag.adapter.TimeLineAdapter
import com.kronos.startup.dag.utils.builderConcatAdapter
import com.kronos.startup.dag.utils.classify
import com.kronos.startup.dag.utils.dpToPx

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/28
 *
 */
class StartupTimeLineActivity : AppCompatActivity(R.layout.startup_activity_dag_time_line) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data =
            intent.getParcelableArrayListExtra(STARTUP_DATA) ?: mutableListOf<StartupTaskData>()
        val map = data.classify()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = builderConcatAdapter {
            add(HeaderAdapter())
            add(TimeLineAdapter(map))
        }
        recyclerView.addItemDecoration(DividerItemDecoration().apply {
            setColorDrawable(Color.parseColor("#1482f0"), 1.dpToPx())
        })
    }
}


fun Context.startupDagActivity(data: MutableList<StartupTaskData>) {
    val intent = Intent(this, StartupTimeLineActivity::class.java)
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



