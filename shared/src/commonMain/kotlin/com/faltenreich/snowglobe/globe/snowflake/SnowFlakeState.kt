package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

data class SnowFlakeState(
    val position: Offset,
    val size: Size,
) {

    val dimension: Rect
        get() = Rect(
            left = position.x,
            top = position.y,
            right = position.x + size.width,
            bottom = position.y + size.height
        )
}