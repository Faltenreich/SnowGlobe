package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faltenreich.snowglobe.globe.debug.GlobeDebugInfo
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
data object Globe

@Composable
fun GlobeScreen(
    viewModel: GlobeViewModel = koinInject(),
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val path = remember { Path() }
    val color = remember { Color.White }

    LaunchedEffect(Unit) { viewModel.start() }

    Scaffold(modifier = modifier) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        viewModel.setup(bounds = coordinates.size.toSize())
                    },
            ) {
                val snowFlakes = state.value.grid.cells.flatMap { it.flatMap { it.snowFlakes } }
                snowFlakes.forEach { snowFlake ->
                    path.addRect(snowFlake.rectangle)
                }
                drawPath(path, color = color)
                path.reset()
            }
            HorizontalDivider()
            GlobeDebugInfo(
                state = state.value,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}