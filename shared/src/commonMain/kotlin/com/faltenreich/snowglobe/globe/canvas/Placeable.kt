package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

interface Placeable {

    val rectangle: Rect
    val cellId: Int

    fun drawOn(path: Path)
}