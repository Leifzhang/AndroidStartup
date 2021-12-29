package com.kronos.startup.dag.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by 码农专栏
 * on 2020-06-04.
 */
class CustomizeScrollView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private var viewListener: OnScrollViewListener? = null

    interface OnScrollViewListener {
        fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int)
    }

    private fun setViewListener(viewListener: OnScrollViewListener?) {
        this.viewListener = viewListener
    }

    private var mRecyclerView: RecyclerView? = null

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView
            setViewListener(Scroller(recyclerView, this))
        }
    }


    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        viewListener?.onScroll(l, t, oldl, oldt)
    }
}

class Scroller(val recyclerView: RecyclerView, val current: View) :
    CustomizeScrollView.OnScrollViewListener {

    override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
        findViewHolder(l)
    }

    private fun findViewHolder(y: Int) {
        val manager = recyclerView.layoutManager as LinearLayoutManager
        val first = manager.findFirstVisibleItemPosition()
        val last = manager.findLastVisibleItemPosition() + 1
        for (index in first until last) {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(index)
            if (viewHolder is AdapterScrollerView) {
                val view = viewHolder.getView()
                if (view != current) {
                    viewHolder.getView().scrollTo(y, 0)
                }
            }
        }
    }

}

interface AdapterScrollerView {
    fun getView(): CustomizeScrollView
}