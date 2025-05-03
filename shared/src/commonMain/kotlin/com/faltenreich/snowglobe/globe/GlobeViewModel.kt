package com.faltenreich.snowglobe.globe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faltenreich.snowglobe.sensor.SensorData
import com.faltenreich.snowglobe.sensor.SensorProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class GlobeViewModel(private val sensorProvider: SensorProvider) : ViewModel() {

    val state = sensorProvider.data.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = SensorData.Zero,
    )

    fun start() {
        sensorProvider.start()
    }

    fun stop() {
        sensorProvider.stop()
    }
}