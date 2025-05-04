package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.random.Random

class SnowFlakeSpawner {

    fun spawn(
        canvas: Size,
        count: Int = COUNT,
        size: Size = Size(width = WIDTH, height = HEIGHT),
    ): List<SnowFlakeState> {
        val random = Random(0)
        return (0 .. count).map {
            val x = random.nextInt(0, canvas.width.toInt()).toFloat()
            val y = random.nextInt(0, canvas.height.toInt()).toFloat()
            SnowFlakeState(
                position = Offset(x = x, y = y),
                size = size,
            )
        }
    }

    companion object {

        private const val COUNT = 100
        private const val WIDTH = 20f
        private const val HEIGHT = 20f
    }
}