package com.faltenreich.snowglobe

import com.faltenreich.snowglobe.globe.globeModule
import com.faltenreich.snowglobe.sensor.sensorModule
import org.koin.dsl.module

fun appModule() = module {
    includes(
        sensorModule(),
        globeModule(),
    )
}