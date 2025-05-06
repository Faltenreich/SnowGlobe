package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Velocity

data class SnowFlake(
    val position: Offset,
    val size: Size,
    val velocity: Velocity,
    val cellId: Int,
) {

    val rectangle: Rect = Rect(
        left = position.x,
        top = position.y,
        right = position.x + size.width,
        bottom = position.y + size.height
    )
}