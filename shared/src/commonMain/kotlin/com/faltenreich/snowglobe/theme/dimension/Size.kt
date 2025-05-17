package com.faltenreich.snowglobe.theme.dimension

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Suppress("ConstructorParameterNaming", "PropertyName")
data class Size(
    val ImageSmaller: Dp = 18.dp,
    val ImageSmall: Dp = 24.dp,
    val ImageMedium: Dp = 28.dp,
    val ImageLarge: Dp = 36.dp,
    val ImageXLarge: Dp = 64.dp,
    val TouchSizeMedium: Dp = 56.dp,
    val TouchSizeLarge: Dp = 64.dp,
)