package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.globe.snowflake.SnowFlakeState
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class GlobeViewModel(private val sensorProvider: SensorProvider) : ViewModel() {

    private val sensorData = sensorProvider.data
    private val canvas = MutableStateFlow(Size.Zero)
    private val snowFlakes = MutableStateFlow(emptyList<SnowFlakeState>())

    val state = snowFlakes.map(::GlobeState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState(snowFlakes = emptyList()),
    )

    init {
        viewModelScope.launch {
            snowFlakes.update {
                (0 until 100).map { index ->
                    SnowFlakeState(
                        coordinates = Rect(
                            left = index.toFloat(),
                            top = index.toFloat(),
                            right = index.toFloat() + 20,
                            bottom = index.toFloat() + 20,
                        )
                    )
                }
            }
        }
        viewModelScope.launch {
            while (true) {
                val sensorData = sensorData.firstOrNull()
                println("Sensor data: $sensorData")
                snowFlakes.update { snowFlakes ->
                    snowFlakes.map { snowFlake ->
                        snowFlake.copy(
                            coordinates = snowFlake.coordinates.copy(
                                top = snowFlake.coordinates.top + 10,
                            ),
                        )
                    }
                }
                delay(1.seconds)
            }
        }
    }

    fun start() {
        sensorProvider.start()
    }

    fun stop() {
        sensorProvider.stop()
    }

    fun setCanvas(size: Size) {
        canvas.update { size }
    }
}