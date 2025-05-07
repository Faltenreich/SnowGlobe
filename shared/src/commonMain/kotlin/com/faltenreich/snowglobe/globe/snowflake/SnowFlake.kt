package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.globe.canvas.Rectangle

data class SnowFlake(
    val rectangle: Rectangle,
    val velocity: Velocity,
    val cellId: Int,
) {

    val rect: Rect = Rect(offset = rectangle.topLeft, size = rectangle.size)
}