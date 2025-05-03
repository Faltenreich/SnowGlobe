package com.faltenreich.snowglobe.sensor

import kotlinx.coroutines.flow.Flow

interface SensorProvider {

    val data: Flow<SensorData>

    fun start()

    fun stop()
}