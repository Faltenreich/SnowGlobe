package com.faltenreich.snowglobe.globe.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.faltenreich.snowglobe.globe.GlobeState

@Composable
fun GlobeDebugInfo(
    state: GlobeState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(all = 16.dp)) {
        Text("Sensor", style = MaterialTheme.typography.titleMedium)
        Row {
            Text("x", modifier = Modifier.weight(1f))
            Text(state.acceleration.x.toString())
        }
        Row {
            Text("y", modifier = Modifier.weight(1f))
            Text(state.acceleration.y.toString())
        }
        Row {
            Text("z", modifier = Modifier.weight(1f))
            Text(state.acceleration.z.toString())
        }
    }
}