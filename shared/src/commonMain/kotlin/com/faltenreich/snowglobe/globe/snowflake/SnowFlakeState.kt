package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Velocity
import kotlin.time.TimeSource

data class SnowFlakeState(
    val position: Offset,
    val size: Size,
    val velocity: Velocity,
    val updatedAt: TimeSource.Monotonic.ValueTimeMark,
) {

    val rectangle: Rect
        get() = Rect(
            left = position.x,
            top = position.y,
            right = position.x + size.width,
            bottom = position.y + size.height
        )
}