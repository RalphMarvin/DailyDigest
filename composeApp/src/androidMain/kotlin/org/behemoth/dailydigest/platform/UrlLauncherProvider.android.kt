package org.behemoth.dailydigest.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity

@Composable
actual fun ProvideUrlLauncher(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val urlLauncher = AndroidUrlLauncher(context as ComponentActivity)
    CompositionLocalProvider(LocalUrlLauncher provides urlLauncher) {
        content()
    }
}