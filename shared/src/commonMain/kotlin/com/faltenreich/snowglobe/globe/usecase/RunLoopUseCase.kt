package com.faltenreich.snowglobe.globe.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.globe.GlobeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
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

        val acceleration = Velocity(
            x = -state.sensorData.x * accelerationScale,
            y = state.sensorData.y * accelerationScale,
        )

        val snowFlakes = state.grid.snowFlakes.groupBy { it.cellId }.flatMap { (_, snowFlakesInCell) ->
            snowFlakesInCell.map { snowFlake ->
                var velocity = Velocity(
                    x = (snowFlake.velocity.x + acceleration.x * secondsElapsed)
                        .coerceIn(-velocityMax, velocityMax),
                    y = (snowFlake.velocity.y + acceleration.y * secondsElapsed)
                        .coerceIn(-velocityMax, velocityMax),
                )

                var position = Offset(
                    x = snowFlake.position.x + velocity.x * secondsElapsed,
                    y = snowFlake.position.y + velocity.y * secondsElapsed,
                )

                position = Offset(
                    x = min(state.grid.size.width - snowFlake.size.width, max(0f, position.x)),
                    y = min(
                        state.grid.size.height - snowFlake.size.height,
                        max(0f, position.y)
                    ),
                )

                val rectangle = Rect(
                    left = position.x,
                    top = position.y,
                    right = position.x + snowFlake.size.width,
                    bottom = position.y + snowFlake.size.height,
                )

                val overlap = snowFlakesInCell.firstOrNull { other ->
                    other != snowFlake && other.rectangle.overlaps(rectangle)
                }

                // TODO: Fix clipping by shifting position
                overlap?.let {
                    position = snowFlake.position
                    val dx = rectangle.center.x - overlap.rectangle.center.x
                    val dy = rectangle.center.y - overlap.rectangle.center.y
                    velocity =
                        if (abs(dx) > abs(dy)) velocity.copy(x = velocity.x * -bounceFactor)
                        else velocity.copy(y = velocity.y * -bounceFactor)
                }

                // TODO: Simplify
                val rectangle2 = Rect(
                    offset = position,
                    size = snowFlake.size,
                )
                val cells = state.grid.cells.flatten()
                val cell = cells.first { rectangle2.overlaps(it.rectangle) }
                snowFlake.copy(
                    cellId = cell.id,
                    position = position,
                    velocity = velocity,
                )
            }
        }
        state.copy(
            updatedAt = now,
            grid = state.grid.copy(
                snowFlakes = snowFlakes,
            ),
        )
    }

    companion object {

        const val ACCELERATION_SCALE = 500f
        const val VELOCITY_MAX = 500f
        const val BOUNCE_FACTOR = .25f
    }
}