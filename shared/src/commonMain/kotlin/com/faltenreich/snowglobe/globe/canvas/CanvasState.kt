package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.time.Clock
import kotlin.time.Instant

data class CanvasState(
    val updatedAt: Instant,
    val grid: Grid,
) {

    companion object {

        val Initial = CanvasState(
            updatedAt = Clock.System.now(),
            grid = Grid(
                rectangle = Rect(
                    offset = Offset.Zero,
                    size = Size.Zero,
                ),
                cells = emptyList(),
                snowFlakes = emptyList(),
            ),
        )
    }
}