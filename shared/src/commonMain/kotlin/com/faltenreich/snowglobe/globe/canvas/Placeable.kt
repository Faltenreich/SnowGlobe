package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

interface Placeable {

    val rectangle: Rect

    fun drawOn(path: Path)
}