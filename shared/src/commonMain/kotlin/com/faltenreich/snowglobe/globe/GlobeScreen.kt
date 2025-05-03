package com.faltenreich.snowglobe.globe

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
data object Globe

@Composable
fun GlobeScreen(modifier: Modifier = Modifier) {
    Text("Hello, World", modifier = modifier)
}