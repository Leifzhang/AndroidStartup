package com.kronos.startup.dag

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * @Author LiABao
 * @Since 2021/1/14
 */
class DividerItemDecoration @JvmOverloads constructor(orientation: Int = LinearLayout.VERTICAL) : RecyclerView.ItemDecoration() {
    /**
     * @return the [Drawable] for this divider.
     */
    var drawable: Drawable? = null
        private set
    private var isHiddenBottom = true

    fun setHiddenBottom(hiddenBottom: Boolean) {
        isHiddenBottom = hiddenBottom
    }

    /**
     * Current orientation. Either [.HORIZONTAL] or [.VERTICAL].
     */
    private var mOrientation = 0
    private val mBounds = Rect()
    private var mDividerHeight = 1

    /**
     * Sets the orientation for this divider. This should be called if
     * [RecyclerView.LayoutManager] changes orientation.
     *
     * @param orientation [.HORIZONTAL] or [.VERTICAL]
     */
    fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL && orientation != VERTICAL)) { "Invalid orientation. It should be either HORIZONTAL or VERTICAL" }
        mOrientation = orientation
    }

    /**
     * Sets the [Drawable] for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    fun setDrawable(drawable: Drawable) {
        requireNotNull(drawable) { "Drawable cannot be null." }
        this.drawable = drawable
    }

    fun setColorDrawable(@ColorInt color: Int, height: Int) {
        drawable = ColorDrawable(color)
        drawable?.setBounds(0, 0, 0, height)
        mDividerHeight = height
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || drawable == null) {
            return
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent, state)
        } else {
            drawHorizontal(c, parent, state)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right,
                    parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        val lastPosition = state.itemCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childRealPosition = parent.getChildAdapterPosition(child)
            if (childRealPosition < lastPosition || !isHiddenBottom) {
                parent.getDecoratedBoundsWithMargins(child, mBounds)
                val bottom = mBounds.bottom + child.translationY.roundToInt()
                val top = bottom - mDividerHeight
                drawable?.setBounds(left, top, right, bottom)
                drawable?.draw(canvas)
            }
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val top: Int
        val bottom: Int
        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            canvas.clipRect(parent.paddingLeft, top,
                    parent.width - parent.paddingRight, bottom)
        } else {
            top = 0
            bottom = parent.height
        }
        val childCount = parent.childCount
        val lastPosition = state.itemCount - 1
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childRealPosition = parent.getChildAdapterPosition(child)
            if (childRealPosition < lastPosition || !isHiddenBottom) {
                parent.layoutManager?.getDecoratedBoundsWithMargins(child, mBounds)
                val right = mBounds.right + child.translationX.roundToInt()
                val left = right - mDividerHeight
                drawable?.setBounds(left, top, right, bottom)
                drawable?.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {
        if (drawable == null) {
            outRect[0, 0, 0] = 0
            return
        }
        if (mOrientation == VERTICAL) {
            outRect[0, 0, 0] = mDividerHeight
        } else {
            outRect[0, 0, mDividerHeight] = 0
        }
    }

    companion object {
        const val HORIZONTAL = LinearLayout.HORIZONTAL
        const val VERTICAL = LinearLayout.VERTICAL
    }

    /**
     * Creates a divider [RecyclerView.ItemDecoration] that can be used with a
     * [LinearLayoutManager].
     *
     * @param orientation Divider orientation. Should be [.HORIZONTAL] or [.VERTICAL].
     */
    init {
        setOrientation(orientation)
    }
}