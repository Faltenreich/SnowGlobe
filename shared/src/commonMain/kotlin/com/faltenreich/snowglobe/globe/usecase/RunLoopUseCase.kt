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

        val snowFlakes = state.snowFlakes.map { snowFlake ->
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
                x = min(state.canvas.width - snowFlake.size.width, max(0f, position.x)),
                y = min(state.canvas.height - snowFlake.size.height, max(0f, position.y)),
            )

            val rectangle = Rect(
                left = position.x,
                top = position.y,
                right = position.x + snowFlake.size.width,
                bottom = position.y + snowFlake.size.height,
            )

            val overlap = state.snowFlakes
                .minus(snowFlake)
                .firstOrNull { other -> other.rectangle.overlaps(rectangle) }

            // TODO: Fix clipping by shifting position
            overlap?.let {
                position = snowFlake.position
                val dx = rectangle.center.x - overlap.rectangle.center.x
                val dy = rectangle.center.y - overlap.rectangle.center.y
                velocity =
                    if (abs(dx) > abs(dy)) velocity.copy(x = velocity.x * -bounceFactor)
                    else velocity.copy(y = velocity.y * -bounceFactor)
            }

            snowFlake.copy(
                position = position,
                velocity = velocity,
            )
        }
        state.copy(snowFlakes = snowFlakes, updatedAt = now)
    }

    companion object {

        const val ACCELERATION_SCALE = 500f
        const val VELOCITY_MAX = 500f
        const val BOUNCE_FACTOR = .25f
    }
}