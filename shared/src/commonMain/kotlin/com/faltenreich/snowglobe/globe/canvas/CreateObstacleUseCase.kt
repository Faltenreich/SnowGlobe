package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size

class CreateObstacleUseCase {

    operator fun invoke(
        size: Size,
        grid: Grid,
    ): Obstacle {
        val rectangle = Rect(
            offset = Offset(
                x = grid.rectangle.center.x - size.width / 2f,
                y = grid.rectangle.center.y - size.height / 2f,
            ),
            size = size,
        )
        val cell = grid.cells.flatten().first { rectangle.overlaps(it.rectangle) }
        return Obstacle(
            rectangle = rectangle,
            cellId = cell.id,
        )
    }
}