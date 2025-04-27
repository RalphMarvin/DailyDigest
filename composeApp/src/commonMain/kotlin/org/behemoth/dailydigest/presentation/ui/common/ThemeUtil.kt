package org.behemoth.dailydigest.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

/**
 * A utility class for managing theme preferences
 */
class ThemeManager(private val settings: Settings) {
    private val _isDarkTheme = mutableStateOf(loadThemePreference())
    val isDarkTheme: MutableState<Boolean> = _isDarkTheme

    private fun loadThemePreference(): Boolean {
        return settings.get(DARK_THEME_KEY, false)
    }

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.value = newValue
        settings.set(DARK_THEME_KEY, newValue)
    }

    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        settings.set(DARK_THEME_KEY, isDark)
    }

    companion object {
        private const val DARK_THEME_KEY = "dark_theme_enabled"
    }
}

/**
 * Composable function to handle theme preferences
 */
@Composable
fun rememberThemeManager(settings: Settings): ThemeManager {
    return remember { ThemeManager(settings) }
}
