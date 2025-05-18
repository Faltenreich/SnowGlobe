package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.random.Random

class CreateObstacleUseCase {

    operator fun invoke(
        size: Size,
        grid: Grid,
    ): Obstacle {
        val random = Random(0)

        val xMin = grid.rectangle.topLeft.x.toInt()
        val xMax = (grid.rectangle.bottomRight.x - size.width).toInt()

        val yMin = grid.rectangle.topLeft.y.toInt()
        val yMax = (grid.rectangle.bottomRight.y - size.height).toInt()

        val x = random.nextInt(xMin, xMax).toFloat()
        val y = random.nextInt(yMin, yMax).toFloat()

        val rectangle = Rect(
            offset = Offset(x = x, y = y),
            size = size,
        )
        return Obstacle(rectangle)
    }
}