package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Velocity

data class SnowFlake(
    override val rectangle: Rect,
    val velocity: Velocity,
    val cellId: Int,
) : Placeable {

    override fun drawOn(path: Path) {
        path.addOval(rectangle)
    }
}