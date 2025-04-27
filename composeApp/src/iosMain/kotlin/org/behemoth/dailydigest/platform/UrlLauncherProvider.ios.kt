package org.behemoth.dailydigest.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
actual fun ProvideUrlLauncher(content: @Composable () -> Unit) {
    val urlLauncher = IosUrlLauncher()
    CompositionLocalProvider(LocalUrlLauncher provides urlLauncher) {
        content()
    }
}