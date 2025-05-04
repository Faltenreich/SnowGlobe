package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import com.faltenreich.snowglobe.globe.snowflake.SnowFlakeState
import com.faltenreich.snowglobe.sensor.SensorData
import kotlin.time.Clock
import kotlin.time.Instant

data class GlobeState(
    val canvas: Size,
    val sensorData: SensorData,
    val snowFlakes: List<SnowFlakeState>,
    val updatedAt: Instant,
) {

    companion object {

        val Initial = GlobeState(
            canvas = Size.Zero,
            sensorData = SensorData.Zero,
            snowFlakes = emptyList(),
            updatedAt = Clock.System.now(),
        )
    }
}