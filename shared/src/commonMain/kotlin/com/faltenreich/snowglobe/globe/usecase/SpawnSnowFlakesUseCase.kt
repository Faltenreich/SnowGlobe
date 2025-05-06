package com.faltenreich.snowglobe.globe.usecase

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Velocity
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class SpawnSnowFlakesUseCase {

    suspend operator fun invoke(
        bounds: Size,
        count: Int = COUNT,
        size: Size = Size(width = WIDTH, height = HEIGHT),
    ): List<SnowFlake> = withContext(Dispatchers.Default) {
        val random = Random(0)
        (0 .. count).map {
            val x = random.nextInt(0, bounds.width.toInt()).toFloat()
            val y = random.nextInt(0, bounds.height.toInt()).toFloat()
            SnowFlake(
                position = Offset(x = x, y = y),
                size = size,
                velocity = Velocity.Zero,
            )
        }
    }

    companion object {

        private const val COUNT = 500
        private const val WIDTH = 10f
        private const val HEIGHT = WIDTH
    }
}