package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    LaunchedEffect(Unit) { viewModel.start() }

    Scaffold { padding ->
        Column(modifier = modifier.padding(padding)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        viewModel.setup(coordinates.size.toSize())
                    },
            ) {
                state.snowFlakes.forEach { snowFlake ->
                    drawCircle(
                        color = Color.White,
                        radius = snowFlake.size.width / 2,
                        center = snowFlake.rectangle.center,
                    )
                }
            }
            HorizontalDivider()
            GlobeDebugInfo(
                state = state,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}