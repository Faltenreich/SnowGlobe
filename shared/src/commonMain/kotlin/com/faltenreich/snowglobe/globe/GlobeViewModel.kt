package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.globe.canvas.BuildGridUseCase
import com.faltenreich.snowglobe.globe.canvas.RunLoopUseCase
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

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

    fun onIntent(intent: GlobeIntent) {
        when (intent) {
            is GlobeIntent.Toggle -> _state.update { it.copy(isRunning = !it.isRunning) }
        }
    }

    fun prepare(bounds: Size) = viewModelScope.launch {
        val state = _state.value.copy(grid = buildGrid(bounds))
        _state.update { state }
    }

    fun start() {
        sensorProvider.start()
    }

    fun run() = viewModelScope.launch {
        while (true) {
            val acceleration = sensorProvider.acceleration.first()
            val state = runLoop(_state.value.copy(acceleration = acceleration))
            _state.update { state }
            // 60 FPS
            delay(16.milliseconds)
        }
    }

    fun stop() {
        sensorProvider.stop()
    }
}