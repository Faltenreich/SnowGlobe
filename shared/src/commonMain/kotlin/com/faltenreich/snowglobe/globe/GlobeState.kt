package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.canvas.Grid
import com.faltenreich.snowglobe.sensor.SensorData
import kotlin.time.Clock
import kotlin.time.Instant

data class GlobeState(
    val updatedAt: Instant,
    val sensorData: SensorData,
    val grid: Grid,
) {

    companion object {

        val Initial = GlobeState(
            updatedAt = Clock.System.now(),
            sensorData = SensorData.Zero,
            grid = Grid(
                size = Size.Zero,
                cells = emptyList(),
                snowFlakes = emptyList(),
            ),
        )
    }
}