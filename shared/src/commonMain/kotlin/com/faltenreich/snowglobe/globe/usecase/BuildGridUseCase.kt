package com.faltenreich.snowglobe.globe.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.globe.canvas.Cell
import com.faltenreich.snowglobe.globe.canvas.Grid
import com.faltenreich.snowglobe.globe.canvas.Rectangle
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
    ): Grid = withContext(Dispatchers.Default) {
        val random = Random(0)
        val cellSize = Size(
            width = bounds.width / cellCountPerDimension,
            height = bounds.height / cellCountPerDimension,
        )
        val grid = (0 until cellCountPerDimension).map { row ->
            (0 until cellCountPerDimension).map { column ->
                val rectangle = Rectangle(
                    offset = Offset(
                        x = row * cellSize.width,
                        y = column * cellSize.height,
                    ),
                    size = cellSize,
                )
                Cell(
                    id = (row * cellCountPerDimension) + column,
                    rectangle = rectangle,
                )
            }
        }
        val snowFlakeCountPerCell = snowFlakeCount / (cellCountPerDimension * cellCountPerDimension)
        val snowFlakes = grid.flatMap { cells ->
            cells.flatMap { cell ->
                val xMin = cell.rectangle.topLeft.x.toInt()
                val xMax = (cell.rectangle.bottomRight.x - snowFlakeSize.width).toInt()

                val yMin = cell.rectangle.topLeft.y.toInt()
                val yMax = (cell.rectangle.bottomRight.y - snowFlakeSize.height).toInt()

                (0 until snowFlakeCountPerCell).map {
                    val x = random.nextInt(xMin, xMax).toFloat()
                    val y = random.nextInt(yMin, yMax).toFloat()

                    SnowFlake(
                        cellId = cell.id,
                        rectangle = Rectangle(
                            offset = Offset(x = x, y = y),
                            size = snowFlakeSize,
                        ),
                        velocity = Velocity.Zero,
                    )
                }
            }
        }
        Grid(
            size = bounds,
            cells = grid,
            snowFlakes = snowFlakes,
        )
    }

    companion object {

        private const val CELL_COUNT_PER_DIMENSION = 10
        private const val SNOW_FLAKE_COUNT = 1000
        private const val SNOW_FLAKE_WIDTH = 10f
        private const val SNOW_FLAKE_HEIGHT = SNOW_FLAKE_WIDTH
    }
}