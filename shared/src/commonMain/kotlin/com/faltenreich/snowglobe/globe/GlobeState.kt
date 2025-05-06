package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake
import com.faltenreich.snowglobe.sensor.SensorData
import kotlin.time.Clock
import kotlin.time.Instant

data class GlobeState(
    val updatedAt: Instant,
    val sensorData: SensorData,
    val canvas: Canvas,
) {

    data class Canvas(
        val size: Size,
        val snowFlakes: List<SnowFlake>,
    )

    companion object {

        val Initial = GlobeState(
            updatedAt = Clock.System.now(),
            sensorData = SensorData.Zero,
            canvas = Canvas(
                size = Size.Zero,
                snowFlakes = emptyList(),
            ),
        )
    }
}