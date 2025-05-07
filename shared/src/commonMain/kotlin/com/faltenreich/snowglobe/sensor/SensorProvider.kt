package com.faltenreich.snowglobe.sensor

import kotlinx.coroutines.flow.Flow

interface SensorProvider {

    val acceleration: Flow<Acceleration>

    fun start()

    fun stop()
}