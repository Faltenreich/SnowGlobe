package com.faltenreich.snowglobe.globe.snowflake

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SnowFlake(
    state: SnowFlakeState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "o",
        modifier = modifier,
    )
}