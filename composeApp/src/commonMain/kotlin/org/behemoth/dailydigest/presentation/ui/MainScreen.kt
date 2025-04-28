package org.behemoth.dailydigest.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.behemoth.dailydigest.presentation.navigation.Tabs
import org.behemoth.dailydigest.presentation.theme.DailyDigestTheme
import org.behemoth.dailydigest.presentation.ui.common.ThemeManager
import org.behemoth.dailydigest.presentation.ui.common.ToastHost
import org.behemoth.dailydigest.presentation.ui.common.ToastManager
import org.behemoth.dailydigest.presentation.ui.common.rememberToastManager
import org.behemoth.dailydigest.platform.ProvideNotificationManager
import org.koin.compose.koinInject

// Create composition locals for managers
val LocalToastManager = compositionLocalOf<ToastManager> { error("No ToastManager provided") }
val LocalThemeManager = compositionLocalOf<ThemeManager> { error("No ThemeManager provided") }

@Composable
fun MainScreen() {
    // Inject the theme manager from Koin
    val themeManager = koinInject<ThemeManager>()

    // Get the current theme preference
    val isDarkTheme by themeManager.isDarkTheme

    // Create and remember a toast manager
    val toastManager = rememberToastManager()

    // Apply the theme based on the user's preference
    DailyDigestTheme(darkTheme = isDarkTheme) {
        // Provide the managers through composition locals
        CompositionLocalProvider(
            LocalToastManager provides toastManager,
            LocalThemeManager provides themeManager
        ) {
            // Wrap the content with ToastHost and NotificationManager
            ToastHost(toastManager = toastManager) {
                ProvideNotificationManager {
                    TabNavigator(Tabs.HomeTab) { navigator ->
                        Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    listOf(
                                        Tabs.HomeTab,
                                        Tabs.SourcesTab,
                                        Tabs.LibraryTab,
                                        Tabs.SettingsTab
                                    ).forEach { tab ->
                                        NavigationBarItem(
                                            selected = navigator.current == tab,
                                            onClick = { navigator.current = tab },
                                            icon = {
                                                tab.options.icon?.let {
                                                    Icon(
                                                        painter = it,
                                                        contentDescription = tab.options.title
                                                    )
                                                }
                                            },
                                            label = { Text(tab.options.title) }
                                        )
                                    }
                                }
                            }
                        ) { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                CurrentTab()
                            }
                        }
                    }
                }
            }
        }
    }
}
