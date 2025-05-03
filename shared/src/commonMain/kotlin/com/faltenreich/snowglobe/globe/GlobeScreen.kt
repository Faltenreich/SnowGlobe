package com.faltenreich.snowglobe.globe

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val state = viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.start() }

    Scaffold { padding ->
        Column {
            Text("Hello, World", modifier = modifier.padding(padding))

            BoxWithConstraints(
                modifier = Modifier.onGloballyPositioned { viewModel.setCanvas(it.size.toSize()) },
            ) {
                state.value.snowFlakes.forEach { snowFlake ->
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
        }
    }
}