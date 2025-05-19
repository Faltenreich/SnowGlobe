package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faltenreich.snowglobe.globe.overlay.GlobeOverlay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object Globe

@Composable
fun GlobeScreen(
    viewModel: GlobeViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val path = remember { Path() }
    val color = remember { Color.White }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(Unit) { viewModel.start() }
    DisposableEffect(Unit) { onDispose { viewModel.stop() } }
    LaunchedEffect(canvasSize) {
        if (canvasSize != Size.Zero) {
            viewModel.prepare(canvasSize)
        }
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    )

    BottomSheetScaffold(
        sheetContent = {
            val padding = WindowInsets.systemBars.asPaddingValues()
            GlobeOverlay(
                state = state.value,
                onIntent = viewModel::onIntent,
                modifier = Modifier.padding(
                    start = padding.calculateStartPadding(LayoutDirection.Ltr),
                    bottom = padding.calculateBottomPadding(),
                    end = padding.calculateEndPadding(LayoutDirection.Ltr),
                ),
            )
        },
        modifier = modifier,
        scaffoldState = scaffoldState,
    ) { _ ->
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { canvasSize = it.size.toSize() }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (!scaffoldState.bottomSheetState.isVisible) {
                                scope.launch { scaffoldState.bottomSheetState.show() }
                            }
                        },
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.drag(change.position, dragAmount)
                    }
                },
        ) {
            state.value.canvas.grid.placeables.forEach { placeable ->
                placeable.drawOn(path)
            }
            drawPath(path, color = color)
            path.reset()
        }
    }
}