package fr.ozoneprojects.weatherappkata.core

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class VerticalSpaceItemDecorator(private val verticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        parent.adapter?.let { adapter ->
            if (parent.adapter != null && parent.getChildAdapterPosition(view) != adapter.itemCount - 1) {
                outRect.bottom = verticalSpaceHeight;
            }
        } ?: run {
            outRect.bottom = verticalSpaceHeight
        }
    }

}