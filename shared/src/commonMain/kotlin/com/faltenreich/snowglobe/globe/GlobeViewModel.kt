package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.globe.canvas.BuildGridUseCase
import com.faltenreich.snowglobe.globe.canvas.CanvasState
import com.faltenreich.snowglobe.globe.canvas.RunLoopUseCase
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
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

    private val isRunning = MutableStateFlow(false)
    private val acceleration = sensorProvider.acceleration
    private val canvas = MutableStateFlow(CanvasState.Initial)

    private val _state = combine(
        isRunning,
        acceleration,
        canvas,
        ::GlobeState,
    )
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState.Initial,
    )

    fun onIntent(intent: GlobeIntent) {
        when (intent) {
            is GlobeIntent.Toggle -> toggle()
        }
    }

    fun prepare(bounds: Size) = viewModelScope.launch {
        canvas.update { it.copy(grid = buildGrid(bounds)) }
    }

    private fun toggle() {
        if (isRunning.value) stop()
        else start()
    }

    fun start() {
        sensorProvider.start()
        isRunning.update { true }
        run()
    }

    fun stop() {
        sensorProvider.stop()
        isRunning.update { false }
    }

    private fun run() = viewModelScope.launch {
        while (isRunning.value) {
            val acceleration = sensorProvider.acceleration.first()
            val state = runLoop(canvas.value, acceleration = acceleration)
            canvas.update { state }
            // 60 FPS
            delay(16.milliseconds)
        }
    }
}