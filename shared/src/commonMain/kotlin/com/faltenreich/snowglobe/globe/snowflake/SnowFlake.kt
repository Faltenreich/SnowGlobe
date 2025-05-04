package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity

@Composable
fun SnowFlake(
    state: SnowFlakeState,
    modifier: Modifier = Modifier,
) = with(LocalDensity.current) {
    Box(
        modifier = modifier
            .background(
                shape = CircleShape,
                color = Color.White,
            )
            .size(state.size.toDpSize()),
    )
}