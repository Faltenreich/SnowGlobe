package com.faltenreich.snowglobe.sensor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.time.Duration.Companion.milliseconds

class DemoSensorProvider : SensorProvider {

    private var isRunning = false

    override val data = flow {
        while (true) {
            if (isRunning) {
                val data = SensorData(
                    x = 0f,
                    y = 9f,
                    z = 9f,
                )
                emit(data)
                delay(100.milliseconds)
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun start() {
        isRunning = true
    }

    override fun stop() {
        isRunning = false
    }
}