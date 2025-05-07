package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake

data class Grid(
    val size: Size,
    val cells: List<List<Cell>>,
    val snowFlakes: List<SnowFlake>,
)