package com.faltenreich.snowglobe.sensor

data class Acceleration(
    val x: Float,
    val y: Float,
    val z: Float,
) {

    companion object {

        val Zero = Acceleration(x = 0f, y = 0f, z = 0f)
    }
}