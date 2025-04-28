package org.behemoth.dailydigest.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Primary color as specified: #b59517
private val PrimaryColor = Color(0xFFB59517)
// Lighter version of primary color for secondary
private val SecondaryColor = Color(0xFFD6B93A)

// Reduced elevation values
val LowElevation = 1.dp
val MediumElevation = 2.dp

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF8EFC3),
    onPrimaryContainer = Color(0xFF3D3000),
    secondary = SecondaryColor,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF8DC),
    onSecondaryContainer = Color(0xFF3D3000),
    tertiary = Color(0xFF6B5778),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251431),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFDFCFF),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFFDFCFF),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDFE2EB),
    onSurfaceVariant = Color(0xFF43474E),
    outline = Color(0xFF73777F)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD6B93A),  // Lighter version of primary for dark theme
    onPrimary = Color(0xFF3D3000),
    primaryContainer = Color(0xFF8A7100),  // Darker gold for container in dark theme
    onPrimaryContainer = Color(0xFFF8EFC3),
    secondary = Color(0xFFEAD380),
    onSecondary = Color(0xFF3D3000),
    secondaryContainer = Color(0xFFB59517),
    onSecondaryContainer = Color(0xFFFFF8DC),
    tertiary = Color(0xFFD6BEE4),
    onTertiary = Color(0xFF3B2948),
    tertiaryContainer = Color(0xFF523F5F),
    onTertiaryContainer = Color(0xFFF2DAFF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF1A1C1E),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF1A1C1E),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF43474E),
    onSurfaceVariant = Color(0xFFC3C7CF),
    outline = Color(0xFF8D9199)
)

@Composable
fun DailyDigestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
