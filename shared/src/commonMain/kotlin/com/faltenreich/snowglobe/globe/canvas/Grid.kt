package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Rect
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake

data class Grid(
    val rectangle: Rect,
    val cells: List<List<Cell>>,
    val snowFlakes: List<SnowFlake>,
)