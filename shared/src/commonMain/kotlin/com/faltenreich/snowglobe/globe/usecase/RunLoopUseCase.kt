package com.faltenreich.snowglobe.globe.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.globe.GlobeState
import com.faltenreich.snowglobe.globe.canvas.Rectangle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.time.Clock

class RunLoopUseCase {

    suspend operator fun invoke(
        state: GlobeState,
        accelerationScale: Float = ACCELERATION_SCALE,
        velocityMax: Float = VELOCITY_MAX,
        bounceFactor: Float = BOUNCE_FACTOR,
    ): GlobeState = withContext(Dispatchers.Default) {
        val now = Clock.System.now()
        val secondsElapsed = now.minus(state.updatedAt).inWholeNanoseconds / 1000_000_000f
        val cells = state.grid.cells.flatten()
        val acceleration = Velocity(
            x = -state.acceleration.x * accelerationScale,
            y = state.acceleration.y * accelerationScale,
        )

        val snowFlakes = state.grid.snowFlakes.groupBy { it.cellId }.flatMap { (_, snowFlakesInCell) ->
            snowFlakesInCell.map { snowFlake ->
                var rectangle = snowFlake.rectangle
                var velocity = snowFlake.velocity

                velocity = Velocity(
                    x = (velocity.x + acceleration.x * secondsElapsed).coerceIn(-velocityMax, velocityMax),
                    y = (velocity.y + acceleration.y * secondsElapsed).coerceIn(-velocityMax, velocityMax),
                )

                rectangle = Rectangle(
                    offset = Offset(
                        x = rectangle.topLeft.x + velocity.x * secondsElapsed,
                        y = rectangle.topLeft.y + velocity.y * secondsElapsed,
                    ),
                    size = rectangle.size,
                )

                // Keep in bounds
                rectangle = Rectangle(
                    offset = Offset(
                        x = rectangle.topLeft.x.coerceIn(0f, state.grid.size.width - rectangle.size.width),
                        y = rectangle.topLeft.y.coerceIn(0f, state.grid.size.height - rectangle.size.height),
                    ),
                    size = rectangle.size,
                )

                val overlap = snowFlakesInCell.firstOrNull { it != snowFlake && it.rectangle.overlaps(rectangle) }
                overlap?.let {
                    rectangle = snowFlake.rectangle

                    // Bounce back
                    val dx = rectangle.center.x - overlap.rectangle.center.x
                    val dy = rectangle.center.y - overlap.rectangle.center.y
                    velocity =
                        if (abs(dx) > abs(dy)) velocity.copy(x = velocity.x * -bounceFactor)
                        else velocity.copy(y = velocity.y * -bounceFactor)
                }

                val cell = cells.first { rectangle.overlaps(it.rectangle) }
                snowFlake.copy(
                    cellId = cell.id,
                    rectangle = rectangle,
                    velocity = velocity,
                )
            }
        }
        state.copy(
            updatedAt = now,
            grid = state.grid.copy(snowFlakes = snowFlakes),
        )
    }

    companion object {

        const val ACCELERATION_SCALE = 500f
        const val VELOCITY_MAX = 500f
        const val BOUNCE_FACTOR = .25f
    }
}