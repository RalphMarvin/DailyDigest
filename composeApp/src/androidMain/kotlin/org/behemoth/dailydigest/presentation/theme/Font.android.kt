package org.behemoth.dailydigest.presentation.theme

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import org.behemoth.dailydigest.R

/**
 * Android implementation of the IBM Plex Sans font family using Google Fonts.
 */
actual val IBMPlexSansFamily: FontFamily = run {
    val provider = GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = R.array.com_google_android_gms_fonts_certs
    )
    
    val fontName = GoogleFont("IBM Plex Sans")
    
    FontFamily(
        Font(googleFont = fontName, fontProvider = provider)
    )
}