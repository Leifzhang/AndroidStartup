package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kronos.startup.dag.R
import com.kronos.startup.dag.sql.StartTaskInfo
import com.kronos.startup.dag.sql.isMainThread
import com.kronos.startup.dag.utils.getDayFormat

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/29
 *
 */
class TaskViewAdapter(private val list: MutableList<StartTaskInfo>) :
    RecyclerView.Adapter<DagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DagViewHolder {
        return DagViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.startup_recycler_view_task_duration, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DagViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class DagViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val taskNameTv: TextView = view.findViewById(R.id.taskNameTv)
    private val durationTv: TextView = view.findViewById(R.id.durationTv)
    private val messageTv: TextView = view.findViewById(R.id.messageTv)

    fun bindView(data: StartTaskInfo) {
        taskNameTv.text = data.taskName?.getSimpleTaskName()

        durationTv.text = getDurationText(data.average())
        val message =
            "isMain:${data.threadName.isMainThread()}  isHistory:${data.date == getDayFormat()}"
        messageTv.text = message
    }

    private fun getDurationText(duration: Long): String {
        return "任务耗时:${duration} ms"
    }

    private fun String.getSimpleTaskName(): String {
        val lastIndex = lastIndexOf(".")
        if (lastIndex == -1) {
            return this
        }
        return substring(lastIndex)
    }
}