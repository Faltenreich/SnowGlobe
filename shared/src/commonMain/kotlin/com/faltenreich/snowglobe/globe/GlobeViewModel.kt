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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

class GlobeViewModel(private val sensorProvider: SensorProvider) : ViewModel() {

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
                            // FIXME: Subtract snowFlake.size
                            val x = min(state.canvas.width, max(0f, snowFlake.coordinates.left - state.sensorData.x))
                            val y = min(state.canvas.height, max(0f, snowFlake.coordinates.top + state.sensorData.y))
                            snowFlake.copy(
                                coordinates = snowFlake.coordinates.copy(
                                    top = y,
                                    left = x,
                                ),
                            )
                        }
                    )

                }
                delay(10.milliseconds)
            }
        }
    }

    fun start() {
        sensorProvider.start()
    }

    fun stop() {
        sensorProvider.stop()
    }

    fun setCanvas(canvas: Size) {
        val x = canvas.width / 2f
        val y = canvas.height / 2f
        val width = 20f
        val height = 20f

        _state.update { state ->
            state.copy(
                canvas = canvas,
                // TODO: Add more
                snowFlakes = listOf(
                    SnowFlakeState(
                        coordinates = Rect(
                            left = x,
                            top = y,
                            right = x + width,
                            bottom = y + height,
                        )
                    )
                )
            )
        }
    }
}