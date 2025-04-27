package org.behemoth.dailydigest.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
actual fun ProvideNotificationManager(content: @Composable () -> Unit) {
    val notificationManager = IosNotificationManager()
    CompositionLocalProvider(LocalNotificationManager provides notificationManager) {
        content()
    }
}