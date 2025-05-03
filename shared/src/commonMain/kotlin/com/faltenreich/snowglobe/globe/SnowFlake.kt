package com.faltenreich.snowglobe.globe

data class SnowFlake(
    val position: Position,
) {

    data class Position(
        val x: Float,
        val y: Float,
    )
}