package com.github.wnebyte.chess.ui

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.min

class MyGridLayoutManager(context: Context?, reverseLayout: Boolean)
    : GridLayoutManager(context, 8, LinearLayoutManager.VERTICAL, reverseLayout)
{
    override fun canScrollVertically(): Boolean = false

    override fun canScrollHorizontally(): Boolean = false

    override fun setReverseLayout(reverseLayout: Boolean)
    {
        super.setReverseLayout(reverseLayout)
    }

    override fun onMeasure(recycler: RecyclerView.Recycler, state: RecyclerView.State,
                           widthSpec: Int, heightSpec: Int)
    {
        val width = View.MeasureSpec.getSize(widthSpec)
        val height = View.MeasureSpec.getSize(heightSpec)
        val size = min(width, height)
        val sizeMeasureSpec = View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY)
        super.onMeasure(recycler, state, sizeMeasureSpec, sizeMeasureSpec)
    }
}