package org.behemoth.dailydigest.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalNotificationManager = staticCompositionLocalOf<NotificationManager> { 
    error("No NotificationManager provided") 
}

@Composable
expect fun ProvideNotificationManager(content: @Composable () -> Unit)