package com.faltenreich.snowglobe

import com.faltenreich.snowglobe.globe.globeModule
import org.koin.dsl.module

fun appModule() = module {
    includes(
        globeModule(),
    )
}