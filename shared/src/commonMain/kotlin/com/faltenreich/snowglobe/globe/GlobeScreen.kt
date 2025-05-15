package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
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
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val path = remember { Path() }
    val color = remember { Color.White }

    LaunchedEffect(Unit) { viewModel.start() }
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    viewModel.prepare(coordinates.size.toSize())
                    viewModel.run()
                }
                .pointerInput(Unit) {
                    detectTapGestures(onTap = viewModel::touch)
                },
        ) {
            if (state.showDebugInfo) {
                state.grid.cells.flatten().forEach { cell ->
                    drawLine(
                        color = color,
                        start = cell.rectangle.topLeft,
                        end = cell.rectangle.topRight,
                    )
                    drawLine(
                        color = color,
                        start = cell.rectangle.topRight,
                        end = cell.rectangle.bottomRight,
                    )
                }
            }

            state.grid.snowFlakes.forEach { snowFlake ->
                path.addOval(snowFlake.rectangle)
            }
            drawPath(path, color = color)
            path.reset()
        }

        if (state.showUi) {
            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    BottomAppBar {

                    }
                },
            ) { padding ->
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    if (state.showDebugInfo) {
                        GlobeDebugInfo(
                            state = state,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}