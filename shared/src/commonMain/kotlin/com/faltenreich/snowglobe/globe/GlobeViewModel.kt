package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.globe.usecase.BuildGridUseCase
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
    private val buildGrid: BuildGridUseCase,
    private val runLoop: RunLoopUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(GlobeState.Initial)
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState.Initial,
    )

    fun setup(bounds: Size) = viewModelScope.launch {
        val grid = buildGrid(bounds)
        _state.update { it.copy(grid = grid) }
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
            val update = measureTimedValue { runLoop(_state.value) }
            println("Loop took ${update.duration}")
            _state.update { update.value }
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