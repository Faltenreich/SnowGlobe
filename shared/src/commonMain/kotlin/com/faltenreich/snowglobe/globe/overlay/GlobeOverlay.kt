package com.faltenreich.snowglobe.globe.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.snowglobe.globe.GlobeIntent
import com.faltenreich.snowglobe.globe.GlobeState
import org.jetbrains.compose.resources.stringResource
import snowglobe.shared.generated.resources.Res
import snowglobe.shared.generated.resources.pause
import snowglobe.shared.generated.resources.play

@Composable
fun GlobeOverlay(
    state: GlobeState,
    onIntent: (GlobeIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Button(onClick = { onIntent(GlobeIntent.Toggle) }) {
            Text(stringResource(if (state.isRunning) Res.string.pause else Res.string.play))
        }
    }
}