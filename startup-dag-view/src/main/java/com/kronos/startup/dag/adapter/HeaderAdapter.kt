package com.kronos.startup.dag.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kronos.startup.dag.R
import com.kronos.startup.dag.utils.getSimpleName
import com.kronos.startup.dag.utils.threadSet

/**
 *
 *  @Author LiABao
 *  @Since 2021/12/27
 *
 */
class HeaderAdapter : RecyclerView.Adapter<HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.startup_recycler_view_header, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 1
    }
}

class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val taskGridLayout: RecyclerView = view.findViewById(R.id.taskGridLayout)
    private val adapter = ThreadNameAdapter()

    init {
        val size = threadSet.size
        taskGridLayout.layoutManager = GridLayoutManager(view.context, size)
        taskGridLayout.adapter = adapter
    }


    class ThreadNameAdapter :
        RecyclerView.Adapter<ThreadNameViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThreadNameViewHolder {
            return ThreadNameViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.startup_recycler_view_thread_name, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ThreadNameViewHolder, position: Int) {
            val threadName = threadSet[position]
            holder.bindThreadName(threadName)

        }

        override fun getItemCount(): Int {
            return threadSet.size
        }
    }

    class ThreadNameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val threadNameTv: TextView = itemView.findViewById(R.id.threadNameTv)

        fun bindThreadName(name: String) {
            threadNameTv.text = name.getSimpleName()
        }
    }

}
