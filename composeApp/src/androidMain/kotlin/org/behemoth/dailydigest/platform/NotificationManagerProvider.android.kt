package org.behemoth.dailydigest.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.activity.ComponentActivity

@Composable
actual fun ProvideNotificationManager(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val notificationManager = AndroidNotificationManager(context as ComponentActivity)
    CompositionLocalProvider(LocalNotificationManager provides notificationManager) {
        content()
    }
}