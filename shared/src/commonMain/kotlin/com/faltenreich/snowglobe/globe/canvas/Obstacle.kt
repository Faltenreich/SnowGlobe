package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

data class Obstacle(
    override val rectangle: Rect,
) : Placeable {

    override fun drawOn(path: Path) {
        path.addRect(rectangle)
    }
}