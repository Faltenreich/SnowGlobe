package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.globe.snowflake.SnowFlakeSpawner
import com.faltenreich.snowglobe.globe.usecase.RunLoopUseCase
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.measureTimedValue

class GlobeViewModel(
    private val sensorProvider: SensorProvider,
    private val snowFlakeSpawner: SnowFlakeSpawner,
    private val runLoop: RunLoopUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(GlobeState.Initial)
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState.Initial,
    )

    fun setup(canvas: Size) {
        _state.update { state ->
            state.copy(
                canvas = canvas,
                snowFlakes = snowFlakeSpawner.spawn(canvas)
            )
        }
        observeSensor()
        startLoop()
    }

    private fun observeSensor() = viewModelScope.launch {
        sensorProvider.data.collect { sensorData ->
            _state.update { state ->
                state.copy(sensorData = sensorData)
            }
        }
    }

    private fun startLoop() = viewModelScope.launch {
        while (true) {
            // FIXME: First few times take too long
            val loop = measureTimedValue { runLoop(_state.value) }
            println("Loop took ${loop.duration}")
            _state.update { loop.value }
            // 60 FPS
            delay(16.milliseconds)
        }
    }

    fun start() {
        sensorProvider.start()
    }

    @Suppress("unused")
    fun stop() {
        sensorProvider.stop()
    }
}