package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.startup.dag.R
import com.kronos.startup.dag.data.ThreadTaskData
import com.kronos.startup.dag.utils.getSimpleName
import com.kronos.startup.dag.utils.threadSet

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/27
 *
 */
class EachThreadAdapter :
    RecyclerView.Adapter<ThreadViewHolder>() {

    var taskData: ThreadTaskData? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadViewHolder {
        return ThreadViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.startup_recycler_view_thread_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ThreadViewHolder, position: Int) {
        val threadName = threadSet[position]
        taskData?.apply {
            startTask[threadName]?.apply {
                holder.bindStartTaskList(this)
            }
            endChildTask[threadName]?.apply {
                holder.bindEndTaskList(this)
            }
        }

    }

    override fun getItemCount(): Int {
        return threadSet.size
    }
}

class ThreadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val beginTask: TextView = itemView.findViewById(R.id.beginTask)
    private val endTask: TextView = itemView.findViewById(R.id.endTask)

    fun bindStartTaskList(tasks: MutableList<StartupTaskData>) {
        val span = StringBuilder()
        if (tasks.isNotEmpty()) {
            tasks.forEach {
                span.append(it.taskName?.getSimpleName()).append("\r\n")
            }
        }
        beginTask.text = span
    }

    fun bindEndTaskList(tasks: MutableList<StartupTaskData>) {
        val span = StringBuilder()
        if (tasks.isNotEmpty()) {
            tasks.forEach {
                span.append(it.taskName?.getSimpleName()).append("\r\n")
            }
        }
        endTask.text = span
    }
}

