package com.faltenreich.snowglobe.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobeViewModel(private val sensorProvider: SensorProvider) : ViewModel() {

    private val sensorData = sensorProvider.data
    private val snowFlakes = MutableStateFlow(emptyList<SnowFlake>())

    val state = snowFlakes.map(::GlobeState).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = GlobeState(snowFlakes = emptyList()),
    )

    init {
        viewModelScope.launch {
            snowFlakes.update {
                (0 until 100).map { index ->
                    SnowFlake(
                        position = SnowFlake.Position(
                            x = index.toFloat(),
                            y = index.toFloat(),
                        )
                    )
                }
            }
        }
    }

    fun start() {
        sensorProvider.start()
    }

    fun stop() {
        sensorProvider.stop()
    }
}