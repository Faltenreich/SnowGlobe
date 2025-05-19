package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Offset

class MoveObstacleUseCase {

    operator fun invoke(grid: Grid, position: Offset, dragAmount: Offset): Grid {
        return grid.copy(
            placeables = grid.placeables.map { placeable ->
                when (placeable) {
                    is Obstacle -> if (placeable.rectangle.contains(position)) {
                        placeable.copy(rectangle = placeable.rectangle.translate(dragAmount))
                    } else {
                        placeable
                    }
                    else -> placeable
                }
            },
        )
    }
}