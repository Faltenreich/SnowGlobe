package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.ListRestart
import com.composables.icons.lucide.Lucide
import com.faltenreich.snowglobe.globe.overlay.GlobeDebugInfo
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import dev.chrisbanes.haze.materials.HazeMaterials
import dev.chrisbanes.haze.rememberHazeState
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import snowglobe.shared.generated.resources.Res
import snowglobe.shared.generated.resources.app_name

@Serializable
data object Globe

@Composable
fun GlobeScreen(
    viewModel: GlobeViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val hazeState = rememberHazeState()
    val path = remember { Path() }
    val color = remember { Color.White }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(Unit) { viewModel.start() }
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    LaunchedEffect(canvasSize) {
        if (canvasSize != Size.Zero) {
            viewModel.prepare(canvasSize)
            viewModel.run()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (state.value.showUi) {
                TopAppBar(
                    title = { Text(stringResource(Res.string.app_name)) },
                    modifier = Modifier
                        .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
                        .fillMaxWidth(),
                    colors = TopAppBarDefaults.largeTopAppBarColors(Color.Transparent),
                )
            }
        },
        bottomBar = {
            if (state.value.showUi) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    modifier = Modifier
                        .hazeEffect(state = hazeState, style = HazeMaterials.ultraThin())
                        .fillMaxWidth(),
                ) {
                    NavigationBarItem(
                        selected = false,
                        onClick = viewModel::restart,
                        icon = { Icon(Lucide.ListRestart, contentDescription = null) },
                    )
                }
            }
        },
    ) { _ ->
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { canvasSize = it.size.toSize() }
                .pointerInput(Unit) { detectTapGestures(onTap = viewModel::touch) }
                .hazeSource(state = hazeState),
        ) {
            if (state.value.showDebugInfo) {
                state.value.grid.cells.flatten().forEach { cell ->
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

            state.value.grid.snowFlakes.forEach { snowFlake ->
                path.addOval(snowFlake.rectangle)
            }
            drawPath(path, color = color)
            path.reset()
        }

        // TODO: Place somewhere
        if (state.value.showDebugInfo) {
            GlobeDebugInfo(
                state = state.value,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}