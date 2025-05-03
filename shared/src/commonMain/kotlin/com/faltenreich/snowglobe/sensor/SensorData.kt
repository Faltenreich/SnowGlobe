package com.faltenreich.snowglobe.sensor

data class SensorData(
    val x: Float,
    val y: Float,
    val z: Float,
) {

    companion object {

        val Zero = SensorData(x = 0f, y = 0f, z = 0f)
    }
}