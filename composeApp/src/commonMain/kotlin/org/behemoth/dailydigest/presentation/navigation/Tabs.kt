package org.behemoth.dailydigest.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

sealed class Tabs {
    object HomeTab : Tab {
        override val options: TabOptions
            @Composable
            get() = TabOptions(
                index = 0u,
                title = "Home",
                icon = rememberVectorPainter(Icons.Default.Home)
            )

        @Composable
        override fun Content() {
            org.behemoth.dailydigest.presentation.ui.home.HomeScreen()
        }
    }

    object LibraryTab : Tab {
        override val options: TabOptions
            @Composable
            get() = TabOptions(
                index = 1u,
                title = "Library",
                icon = rememberVectorPainter(Icons.Default.Book)
            )

        @Composable
        override fun Content() {
            org.behemoth.dailydigest.presentation.ui.library.LibraryScreen()
        }
    }

    object SettingsTab : Tab {
        override val options: TabOptions
            @Composable
            get() = TabOptions(
                index = 2u,
                title = "Settings",
                icon = rememberVectorPainter(Icons.Default.Settings)
            )

        @Composable
        override fun Content() {
            org.behemoth.dailydigest.presentation.ui.settings.SettingsScreen()
        }
    }
}
