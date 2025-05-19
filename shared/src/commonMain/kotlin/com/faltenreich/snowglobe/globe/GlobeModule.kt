package com.faltenreich.snowglobe.globe

import com.faltenreich.snowglobe.globe.canvas.BuildGridUseCase
import com.faltenreich.snowglobe.globe.canvas.CreateObstacleUseCase
import com.faltenreich.snowglobe.globe.canvas.RunLoopUseCase
import com.faltenreich.snowglobe.globe.canvas.MoveObstacleUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun globeModule() = module {
    factoryOf(::BuildGridUseCase)
    factoryOf(::CreateObstacleUseCase)
    factoryOf(::MoveObstacleUseCase)

    factoryOf(::RunLoopUseCase)

    viewModelOf(::GlobeViewModel)
}