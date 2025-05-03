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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class GlobeViewModel(private val sensorProvider: SensorProvider) : ViewModel() {

    private val _state = MutableStateFlow(GlobeState.Initial)
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState.Initial,
    )

    init {
        viewModelScope.launch {
            sensorProvider.data.collectLatest { sensorData ->
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
                            snowFlake.copy(
                                coordinates = snowFlake.coordinates.copy(
                                    top = snowFlake.coordinates.top + (state.sensorData.y * 10),
                                    left = snowFlake.coordinates.top + (state.sensorData.x * 10),
                                ),
                            )
                        }
                    )

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

    fun setCanvas(canvas: Size) {
        val x = canvas.width / 2f
        val y = canvas.height / 2f
        val width = 20f
        val height = 20f

        _state.update { state ->
            state.copy(
                canvas = canvas,
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