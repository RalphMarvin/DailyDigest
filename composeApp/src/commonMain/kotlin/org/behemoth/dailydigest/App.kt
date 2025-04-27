package org.behemoth.dailydigest

import androidx.compose.runtime.Composable
import org.behemoth.dailydigest.di.initKoin
import org.behemoth.dailydigest.presentation.ui.MainScreen
import org.behemoth.dailydigest.platform.ProvideUrlLauncher
import org.jetbrains.compose.ui.tooling.preview.Preview

// Initialize Koin
private val koinApp = initKoin()

@Composable
@Preview
fun App() {
    ProvideUrlLauncher {
        MainScreen()
    }
}
