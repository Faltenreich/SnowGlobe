package com.faltenreich.snowglobe.sensor

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun sensorModule() = module {
    singleOf(::DemoSensorProvider) bind SensorProvider::class
}