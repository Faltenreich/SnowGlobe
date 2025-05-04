package com.faltenreich.snowglobe.globe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Velocity
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
import kotlin.time.Clock
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
                    // FIXME: Velocity starts too slow and builds up too high
                    val now = Clock.System.now()
                    val deltaTime = now.minus(state.updatedAt).inWholeNanoseconds / 1_000_000_000f

                    state.copy(
                        snowFlakes = state.snowFlakes.map { snowFlake ->
                            val acceleration = Velocity(
                                x = -state.sensorData.x,
                                y = state.sensorData.y,
                            )

                            val velocity = Velocity(
                                x = snowFlake.velocity.x + acceleration.x * deltaTime,
                                y = snowFlake.velocity.y + acceleration.y * deltaTime,
                            )

                            val position = Offset(
                                x = snowFlake.position.x + velocity.x * deltaTime + .5f * acceleration.x * deltaTime * deltaTime,
                                y = snowFlake.position.y + velocity.y * deltaTime + .5f * acceleration.y * deltaTime * deltaTime,
                            )

                            val bounds = Rect(
                                left = 0f,
                                top = 0f,
                                right = state.canvas.width - snowFlake.size.width,
                                bottom = state.canvas.height - snowFlake.size.height,
                            )

                            val positionInBounds = Offset(
                                x = min(bounds.right, max(bounds.left, position.x)),
                                y = min(bounds.bottom, max(bounds.top, position.y)),
                            )

                            val rectangle = Rect(
                                left = positionInBounds.x,
                                top = positionInBounds.y,
                                right = positionInBounds.x + snowFlake.size.width,
                                bottom = positionInBounds.y + snowFlake.size.height,
                            )

                            val overlaps = state.snowFlakes
                                .minus(snowFlake)
                                .any { other -> other.rectangle.overlaps(rectangle) }
                            snowFlake.copy(
                                position = if (overlaps) snowFlake.position else rectangle.topLeft,
                                velocity = velocity,
                            )
                        },
                        updatedAt = now,
                    )
                }
                // TODO: Determine frame rate
                delay(1.milliseconds)
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