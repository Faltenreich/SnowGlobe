package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.canvas.CanvasState
import com.faltenreich.snowglobe.globe.canvas.Grid
import com.faltenreich.snowglobe.sensor.Acceleration
import kotlin.time.Clock

data class GlobeState(
    val isRunning: Boolean,
    val acceleration: Acceleration,
    val canvas: CanvasState,
) {

    companion object {

        val Initial = GlobeState(
            isRunning = false,
            acceleration = Acceleration.Zero,
            canvas = CanvasState(
                updatedAt = Clock.System.now(),
                grid = Grid(
                    rectangle = Rect(
                        offset = Offset.Zero,
                        size = Size.Zero,
                    ),
                    cells = emptyList(),
                    snowFlakes = emptyList(),
                ),
            ),
        )
    }
}