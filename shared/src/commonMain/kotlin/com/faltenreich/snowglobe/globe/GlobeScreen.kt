package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faltenreich.snowglobe.globe.debug.GlobeDebugInfo
import com.faltenreich.snowglobe.globe.snowflake.SnowFlake
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .onGloballyPositioned { coordinates ->
                        viewModel.setCanvas(coordinates.size.toSize())
                    },
            ) {
                state.snowFlakes.forEach { snowFlake ->
                    SnowFlake(
                        state = snowFlake,
                        modifier = Modifier.offset {
                            val topLeft = snowFlake.coordinates.topLeft
                            IntOffset(
                                x = topLeft.x.toInt(),
                                y = topLeft.y.toInt(),
                            )
                        }
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