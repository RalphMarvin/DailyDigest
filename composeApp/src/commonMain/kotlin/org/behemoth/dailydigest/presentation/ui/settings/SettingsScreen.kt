package org.behemoth.dailydigest.presentation.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.russhwolf.settings.Settings
import org.behemoth.dailydigest.platform.LocalNotificationManager
import org.behemoth.dailydigest.presentation.ui.LocalThemeManager
import org.behemoth.dailydigest.presentation.ui.common.CenteredTopAppBar
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    // Get managers from composition locals
    val themeManager = LocalThemeManager.current
    val notificationManager = LocalNotificationManager.current
    val settings: Settings = koinInject()

    // Theme state
    val darkModeEnabled by themeManager.isDarkTheme

    // Notification state
    var notificationsEnabled by remember { 
        mutableStateOf(settings.getBoolean("notifications_enabled", false)) 
    }

    // Effect to handle notification permission
    LaunchedEffect(notificationsEnabled) {
        if (notificationsEnabled) {
            notificationManager.requestPermission()
            if (notificationManager.isPermissionGranted()) {
                notificationManager.scheduleDailyNewsReminder()
                settings.putBoolean("notifications_enabled", true)
            } else {
                notificationsEnabled = false
                settings.putBoolean("notifications_enabled", false)
            }
        } else {
            notificationManager.cancelDailyNewsReminder()
            settings.putBoolean("notifications_enabled", false)
        }
    }

    Scaffold(
        topBar = {
            CenteredTopAppBar(
                title = "Settings"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Dark Mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Enable dark theme",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = darkModeEnabled,
                    onCheckedChange = { themeManager.toggleTheme() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Daily News Reminder",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Get daily reminder at 7:30 AM to read your news",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "About",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Daily Digest",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Version 1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "A news app built with Kotlin Multiplatform for Android and iOS by Behemoth Lab",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
