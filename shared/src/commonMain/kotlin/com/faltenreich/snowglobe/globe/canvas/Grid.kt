package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Rect

data class Grid(
    val rectangle: Rect,
    val cells: List<List<Cell>>,
    val snowFlakes: List<SnowFlake>,
)