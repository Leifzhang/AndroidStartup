package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kronos.lib.startup.data.StartupTaskData
import com.kronos.startup.dag.R
import com.kronos.startup.dag.utils.gone
import com.kronos.startup.dag.utils.visible

/**
 *
 *  @Author LiABao
 *  @Since 2021/11/29
 *
 */
class DagViewAdapter(private val list: MutableList<StartupTaskData>) :
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

    fun bindView(data: StartupTaskData) {
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
        durationTv.text = getDurationText(data.duration)
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