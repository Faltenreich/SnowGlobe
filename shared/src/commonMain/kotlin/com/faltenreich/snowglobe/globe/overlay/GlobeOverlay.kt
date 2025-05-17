package com.faltenreich.snowglobe.globe.overlay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pause
import com.composables.icons.lucide.Play
import com.faltenreich.snowglobe.globe.GlobeIntent
import com.faltenreich.snowglobe.globe.GlobeState
import com.faltenreich.snowglobe.theme.LocalDimensions
import org.jetbrains.compose.resources.stringResource
import snowglobe.shared.generated.resources.Res
import snowglobe.shared.generated.resources.pause
import snowglobe.shared.generated.resources.play

@Composable
fun GlobeOverlay(
    state: GlobeState,
    onIntent: (GlobeIntent) -> Unit,
    modifier: Modifier = Modifier,
) = with(state) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = LocalDimensions.current.padding.P_2),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconToggleButton(
            checked = isRunning,
            onCheckedChange = { onIntent(GlobeIntent.Toggle) },
        ) {
            Icon(
                imageVector = if (isRunning) Lucide.Pause else Lucide.Play,
                contentDescription = stringResource(if (isRunning) Res.string.pause else Res.string.play),
            )
        }
    }
}