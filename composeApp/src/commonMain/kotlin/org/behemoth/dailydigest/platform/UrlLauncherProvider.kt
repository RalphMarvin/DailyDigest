package org.behemoth.dailydigest.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalUrlLauncher = staticCompositionLocalOf<UrlLauncher> { 
    error("No UrlLauncher provided") 
}

@Composable
expect fun ProvideUrlLauncher(content: @Composable () -> Unit)