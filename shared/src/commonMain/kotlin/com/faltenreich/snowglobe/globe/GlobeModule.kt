package com.faltenreich.snowglobe.globe

import com.faltenreich.snowglobe.globe.usecase.BuildGridUseCase
import com.faltenreich.snowglobe.globe.usecase.RunLoopUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun globeModule() = module {
    factoryOf(::BuildGridUseCase)

    factoryOf(::RunLoopUseCase)

    viewModelOf(::GlobeViewModel)
}