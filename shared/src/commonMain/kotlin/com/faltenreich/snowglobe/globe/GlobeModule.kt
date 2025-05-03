package com.faltenreich.snowglobe.globe

import com.faltenreich.snowglobe.globe.snowflake.SnowFlakeSpawner
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun globeModule() = module {
    factoryOf(::SnowFlakeSpawner)

    viewModelOf(::GlobeViewModel)
}