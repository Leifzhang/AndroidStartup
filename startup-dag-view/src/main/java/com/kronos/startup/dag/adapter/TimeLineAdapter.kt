package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kronos.startup.dag.R
import com.kronos.startup.dag.data.ThreadTaskData
import com.kronos.startup.dag.utils.threadSet
import com.kronos.startup.dag.widget.AdapterScrollerView
import com.kronos.startup.dag.widget.CustomizeScrollView
import com.kronos.startup.dag.widget.getRecyclerView

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/27
 *
 */
class TimeLineAdapter(private val list: MutableList<ThreadTaskData>) :
    RecyclerView.Adapter<TimeLineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return TimeLineViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.startup_recycler_view_time_line, parent, false), parent
        )
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


class TimeLineViewHolder(view: View, private val parent: ViewGroup) : RecyclerView.ViewHolder(view),
    AdapterScrollerView {

    private val taskGridLayout: RecyclerView = view.findViewById(R.id.taskGridLayout)
    private val customScrollView: CustomizeScrollView = view.findViewById(R.id.customScrollView)
    private val adapter = EachThreadAdapter()

    init {
        val size = threadSet.size
        taskGridLayout.layoutManager = GridLayoutManager(view.context, size)
        val recyclerView = parent.getRecyclerView()
        recyclerView?.apply {
            customScrollView.attachToRecyclerView(this)
        }
    }

    fun bindView(taskData: ThreadTaskData) {
        adapter.taskData = taskData
        taskGridLayout.adapter = adapter
    }

    private fun String.getSimpleTaskName(): String {
        val lastIndex = lastIndexOf(".")
        if (lastIndex == -1) {
            return this
        }
        return substring(lastIndex)
    }

    override fun getView(): CustomizeScrollView {
        return customScrollView
    }
}