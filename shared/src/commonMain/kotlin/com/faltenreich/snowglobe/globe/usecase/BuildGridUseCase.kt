package com.faltenreich.snowglobe.globe.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.globe.GlobeState
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class BuildGridUseCase {

    suspend operator fun invoke(
        bounds: Size,
        count: Int = SNOW_FLAKE_COUNT,
        size: Size = Size(width = SNOW_FLAKE_WIDTH, height = SNOW_FLAKE_HEIGHT),
    ): GlobeState.Grid = withContext(Dispatchers.Default) {
        val random = Random(0)
        val snowFlakes = (0 .. count).map {
            val x = random.nextInt(0, bounds.width.toInt()).toFloat()
            val y = random.nextInt(0, bounds.height.toInt()).toFloat()
            SnowFlake(
                position = Offset(x = x, y = y),
                size = size,
                velocity = Velocity.Zero,
            )
        }

        val cell = GlobeState.Cell(
            rectangle = Rect(
                offset = Offset.Zero,
                size = bounds,
            ),
            snowFlakes = snowFlakes,
        )
        val cells = listOf(cell)
        GlobeState.Grid(
            size = bounds,
            cells = cells
        )
    }

    companion object {

        private const val SNOW_FLAKE_COUNT = 500
        private const val SNOW_FLAKE_WIDTH = 10f
        private const val SNOW_FLAKE_HEIGHT = SNOW_FLAKE_WIDTH
    }
}