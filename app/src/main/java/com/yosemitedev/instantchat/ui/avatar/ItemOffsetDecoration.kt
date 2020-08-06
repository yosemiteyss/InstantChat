package com.yosemitedev.instantchat.ui.avatar

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView

class ItemOffsetDecoration(
    private val context: Context,
    @DimenRes private val itemOffsetDimen: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemOffset = context.resources.getDimensionPixelOffset(itemOffsetDimen)
        outRect.set(itemOffset, itemOffset, itemOffset, itemOffset)
    }
}