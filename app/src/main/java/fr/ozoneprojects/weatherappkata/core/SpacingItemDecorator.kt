package fr.ozoneprojects.weatherappkata.core

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

open class SpacingItemDecorator(
    left: Int = 0,
    top: Int = 0,
    right: Int = 0,
    bottom: Int = 0
) : RecyclerView.ItemDecoration() {

    constructor(
        left: Float = 0f,
        top: Float = 0f,
        right: Float = 0f,
        bottom: Float = 0f
    ) : this(left.roundToInt(), top.roundToInt(), right.roundToInt(), bottom.roundToInt())

    constructor(spacing: Int) : this(spacing, spacing, spacing, spacing)

    private val spacingRect = Rect()

    init {
        spacingRect.set(left, top, right, bottom)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(spacingRect)
    }
}