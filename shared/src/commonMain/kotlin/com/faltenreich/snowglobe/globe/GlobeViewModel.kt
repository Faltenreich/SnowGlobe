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
import kotlin.random.Random
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

    fun start() {
        sensorProvider.start()
    }

    fun stop() {
        sensorProvider.stop()
    }

    fun setCanvas(canvas: Size) {
        val random = Random(0)
        val width = 20f
        val height = 20f
        val count = 100

        _state.update { state ->
            state.copy(
                canvas = canvas,
                snowFlakes = (0 .. count).map {
                    val x = random.nextInt(0, canvas.width.toInt()).toFloat()
                    val y = random.nextInt(0, canvas.height.toInt()).toFloat()
                    SnowFlakeState(
                        coordinates = Rect(
                            left = x,
                            top = y,
                            right = x + width,
                            bottom = y + height,
                        )
                    )
                }
            )
        }
    }
}