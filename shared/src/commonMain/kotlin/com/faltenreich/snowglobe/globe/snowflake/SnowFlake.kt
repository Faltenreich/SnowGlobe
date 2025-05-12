package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Velocity

data class SnowFlake(
    val rectangle: Rect,
    val velocity: Velocity,
    val cellId: Int,
)