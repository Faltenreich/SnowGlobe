package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.globe.snowflake.SnowFlakeSpawner
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

class GlobeViewModel(
    private val sensorProvider: SensorProvider,
    private val snowFlakeSpawner: SnowFlakeSpawner,
) : ViewModel() {

    private val _state = MutableStateFlow(GlobeState.Initial)
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState.Initial,
    )

    init {
        viewModelScope.launch {
            sensorProvider.data.collect { sensorData ->
                _state.update { state ->
                    state.copy(sensorData = sensorData)
                }
            }
        }
        viewModelScope.launch {
            while (true) {
                _state.update { state ->
                    state.copy(
                        snowFlakes = state.snowFlakes.map { snowFlake ->
                            val width = snowFlake.coordinates.width
                            val height = snowFlake.coordinates.height
                            val (xMin, xMax) = 0f to state.canvas.width - width
                            val (yMin, yMax) = 0f to state.canvas.height - height
                            val x = min(xMax, max(xMin, snowFlake.coordinates.left - state.sensorData.x))
                            val y = min(yMax, max(yMin, snowFlake.coordinates.top + state.sensorData.y))
                            snowFlake.copy(
                                coordinates = snowFlake.coordinates.copy(
                                    left = x,
                                    top = y,
                                    right = x + width,
                                    bottom = y + height,
                                ),
                            )
                        }
                    )
                }
                delay(10.milliseconds)
            }
        }
    }

    fun setup(canvas: Size) {
        _state.update { state ->
            state.copy(
                canvas = canvas,
                snowFlakes = snowFlakeSpawner.spawn(canvas)
            )
        }
    }

    fun start() {
        sensorProvider.start()
    }

    fun stop() {
        sensorProvider.stop()
    }
}