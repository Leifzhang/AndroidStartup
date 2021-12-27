package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kronos.startup.dag.R
import com.kronos.startup.dag.data.ThreadTaskData
import com.kronos.startup.dag.utils.threadSet

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
                .inflate(R.layout.startup_recycler_view_time_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bindView(list[position])
    }


    override fun getItemCount(): Int {
        return list.size
    }
}


class TimeLineViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val taskGridLayout: RecyclerView = view.findViewById(R.id.taskGridLayout)
    private val adapter = EachThreadAdapter()

    init {
        val size = threadSet.size
        taskGridLayout.layoutManager = GridLayoutManager(view.context, size)

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
}