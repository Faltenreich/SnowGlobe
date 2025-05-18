package com.faltenreich.snowglobe.globe.canvas

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.sensor.Acceleration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.time.Clock

class RunLoopUseCase {

    suspend operator fun invoke(
        state: CanvasState,
        acceleration: Acceleration,
        accelerationScale: Float = ACCELERATION_SCALE,
        velocityMax: Float = VELOCITY_MAX,
        bounceFactor: Float = BOUNCE_FACTOR,
    ): CanvasState = withContext(Dispatchers.Default) {
        val now = Clock.System.now()
        val secondsElapsed = now.minus(state.updatedAt).inWholeNanoseconds / 1000_000_000f
        val acceleration = Velocity(
            x = -acceleration.x * accelerationScale,
            y = acceleration.y * accelerationScale,
        )

        val cells = state.grid.cells.flatten()
        val placeables = mutableListOf<Placeable>()

        val obstacles = state.grid.placeables.filterIsInstance<Obstacle>()
        placeables.addAll(obstacles)

        val snowFlakes = state.grid.placeables.filterIsInstance<SnowFlake>()
        snowFlakes.groupBy { it.cellId }.flatMap { (_, snowFlakesInCell) ->
            snowFlakesInCell.map { snowFlake ->
                var rectangle = snowFlake.rectangle
                var velocity = snowFlake.velocity

                velocity = Velocity(
                    x = (velocity.x + acceleration.x * secondsElapsed).coerceIn(-velocityMax, velocityMax),
                    y = (velocity.y + acceleration.y * secondsElapsed).coerceIn(-velocityMax, velocityMax),
                )

                rectangle = rectangle.translate(
                    offset = Offset(
                        x = velocity.x * secondsElapsed,
                        y = velocity.y * secondsElapsed,
                    ),
                )

                if (rectangle.isLeaving(state.grid.rectangle)) {
                    // Keep in bounds
                    rectangle = snowFlake.rectangle
                    velocity = snowFlake.velocity * -bounceFactor
                } else {
                    val overlap = snowFlakesInCell.firstOrNull { it != snowFlake && it.rectangle.overlaps(rectangle) }
                    overlap?.let {
                        // Bounce back
                        rectangle = snowFlake.rectangle
                        val dx = rectangle.center.x - overlap.rectangle.center.x
                        val dy = rectangle.center.y - overlap.rectangle.center.y
                        velocity =
                            if (abs(dx) > abs(dy)) velocity.copy(x = velocity.x * -bounceFactor)
                            else velocity.copy(y = velocity.y * -bounceFactor)
                    }
                }

                val cell = cells.first { rectangle.overlaps(it.rectangle) }
                placeables.add(
                    snowFlake.copy(
                        cellId = cell.id,
                        rectangle = rectangle,
                        velocity = velocity,
                    )
                )
            }
        }
        state.copy(
            updatedAt = now,
            grid = state.grid.copy(placeables = placeables),
        )
    }

    private fun Rect.isLeaving(other: Rect): Boolean {
        return left < other.left ||top < other.top || right > other.right || bottom > other.bottom
    }

    companion object {

        const val ACCELERATION_SCALE = 500f
        const val VELOCITY_MAX = 500f
        const val BOUNCE_FACTOR = .25f
    }
}