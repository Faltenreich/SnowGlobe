package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake
import com.faltenreich.snowglobe.sensor.SensorData
import kotlin.time.Clock
import kotlin.time.Instant

data class GlobeState(
    val updatedAt: Instant,
    val sensorData: SensorData,
    val grid: Grid,
) {

    data class Grid(
        val size: Size,
        val cells: List<List<Cell>>,
        val snowFlakes: List<SnowFlake>,
    )

    data class Cell(
        val id: Int,
        val rectangle: Rect,
    )

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