package org.behemoth.dailydigest.presentation.theme

import androidx.compose.ui.text.font.FontFamily

/**
 * iOS implementation of the IBM Plex Sans font family.
 * Since we can't use Google Fonts directly on iOS, we fall back to the default font family.
 * In a real app, you would bundle the font files with the app and load them here.
 */
actual val IBMPlexSansFamily: FontFamily = FontFamily.Default