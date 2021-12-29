package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kronos.startup.dag.R
import com.kronos.startup.dag.data.ThreadTaskData
import com.kronos.startup.dag.utils.threadSet
import com.kronos.startup.dag.widget.gone
import com.kronos.startup.dag.widget.visible

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/29
 *
 */
class DagViewAdapter(private val list: MutableList<ThreadTaskData>) :
    RecyclerView.Adapter<DagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DagViewHolder {
        return DagViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.startup_recycler_view_dag, parent, false)
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
    private val messageTv: TextView = view.findViewById(R.id.messageTv)
    private val dependenciesTv: TextView = view.findViewById(R.id.dependenciesTv)
    private val durationTv: TextView = view.findViewById(R.id.durationTv)
    private val dependenciesLayout: LinearLayout = view.findViewById(R.id.dependenciesLayout)
    private val startTaskTv: TextView = view.findViewById(R.id.startTaskTv)
    private val endTaskTv: TextView = view.findViewById(R.id.endTaskTv)
    private val taskGridLayout: RecyclerView = view.findViewById(R.id.taskGridLayout)
    private val adapter = EachThreadAdapter()

    init {
        val size = threadSet.size
        taskGridLayout.layoutManager = GridLayoutManager(view.context, size)
        taskGridLayout.adapter = adapter
    }

    fun bindView(taskData: ThreadTaskData) {
        val data = taskData.mainTaskData
        taskNameTv.text = data.taskName?.getSimpleTaskName()
        messageTv.text = data.message
        if (data.dependencies.isNotEmpty()) {
            dependenciesLayout.visible()
            val stringBuilder = StringBuilder()
            stringBuilder.append("[")
            data.dependencies.forEach {
                stringBuilder.append(it.getSimpleTaskName()).append(",")
            }
            stringBuilder.append("]")
            dependenciesTv.text = stringBuilder.toString()
        } else {
            dependenciesLayout.gone()
        }
        val info = taskData.getStartTaskInfo()
        if (info.isNotEmpty()) {
            startTaskTv.visible()
            startTaskTv.text = info
        } else {
            startTaskTv.gone()
        }
        val endInfo = taskData.getEndTaskInfo()
        if (endInfo.isNotEmpty()) {
            endTaskTv.visible()
            endTaskTv.text = endInfo
        } else {
            endTaskTv.gone()
        }
        durationTv.text = getDurationText(data.duration)
        adapter.taskData = taskData
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