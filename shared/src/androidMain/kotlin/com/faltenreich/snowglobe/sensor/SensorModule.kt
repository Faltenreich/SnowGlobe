package com.faltenreich.snowglobe.sensor

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun sensorModule() = module {
    factoryOf(::AndroidSensorProvider) bind SensorProvider::class
}