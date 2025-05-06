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
        snowFlakeCount: Int = SNOW_FLAKE_COUNT,
        snowFlakeSize: Size = Size(width = SNOW_FLAKE_WIDTH, height = SNOW_FLAKE_HEIGHT),
        cellCountPerDimension: Int = CELL_COUNT_PER_DIMENSION,
    ): GlobeState.Grid = withContext(Dispatchers.Default) {
        val random = Random(0)
        val cellSize = Size(
            width = bounds.width / cellCountPerDimension,
            height = bounds.height / cellCountPerDimension,
        )
        val snowFlakeCountPerCell = snowFlakeCount / (cellCountPerDimension * cellCountPerDimension)
        val cells = (0 until cellCountPerDimension).map { column ->
            (0 until cellCountPerDimension).map { row ->
                val rectangle = Rect(
                    offset = Offset(
                        x = column * cellSize.width,
                        y = row * cellSize.height,
                    ),
                    size = cellSize,
                )
                val snowFlakes = (0 until snowFlakeCountPerCell).map {
                    val x = random.nextInt(rectangle.topLeft.x.toInt(), rectangle.bottomRight.x.toInt()).toFloat()
                    val y = random.nextInt(rectangle.topLeft.y.toInt(), rectangle.bottomRight.y.toInt()).toFloat()
                    SnowFlake(
                        position = Offset(x = x, y = y),
                        size = snowFlakeSize,
                        velocity = Velocity.Zero,
                    )
                }
                GlobeState.Cell(
                    rectangle = rectangle,
                    snowFlakes = snowFlakes,
                )
            }
        }
        GlobeState.Grid(
            size = bounds,
            cells = cells
        )
    }

    companion object {

        private const val CELL_COUNT_PER_DIMENSION = 10
        private const val SNOW_FLAKE_COUNT = 2000
        private const val SNOW_FLAKE_WIDTH = 10f
        private const val SNOW_FLAKE_HEIGHT = SNOW_FLAKE_WIDTH
    }
}