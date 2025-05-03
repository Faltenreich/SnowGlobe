package com.faltenreich.snowglobe.globe

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun globeModule() = module {
    viewModelOf(::GlobeViewModel)
}