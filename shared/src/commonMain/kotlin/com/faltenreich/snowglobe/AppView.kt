package com.faltenreich.snowglobe

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.faltenreich.snowglobe.globe.Globe
import com.faltenreich.snowglobe.globe.GlobeScreen

@Composable
fun AppView(modifier: Modifier = Modifier) {
    AppTheme {
        Surface(modifier = modifier.fillMaxSize()) {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Globe,
            ) {
                composable<Globe> { GlobeScreen() }
            }
        }
    }
}