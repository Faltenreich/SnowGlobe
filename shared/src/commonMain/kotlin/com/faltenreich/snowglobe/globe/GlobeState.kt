package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.canvas.Grid
import com.faltenreich.snowglobe.sensor.Acceleration
import kotlin.time.Clock
import kotlin.time.Instant

data class GlobeState(
    val updatedAt: Instant,
    val acceleration: Acceleration,
    val grid: Grid,
    val showDebugInfo: Boolean = false,
) {

    companion object {

        val Initial = GlobeState(
            updatedAt = Clock.System.now(),
            acceleration = Acceleration.Zero,
            grid = Grid(
                rectangle = Rect(
                    offset = Offset.Zero,
                    size = Size.Zero,
                ),
                cells = emptyList(),
                snowFlakes = emptyList(),
            ),
        )
    }
}