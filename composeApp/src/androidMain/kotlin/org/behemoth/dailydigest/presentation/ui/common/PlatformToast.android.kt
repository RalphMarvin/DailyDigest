package org.behemoth.dailydigest.presentation.ui.common

import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Android implementation of PlatformToast using Material3 Snackbar
 */
@Composable
actual fun PlatformToast(toast: ToastMessage) {
    val context = LocalContext.current
    val duration = when (toast.duration) {
        ToastDuration.SHORT -> Toast.LENGTH_SHORT
        ToastDuration.LONG -> Toast.LENGTH_LONG
    }

    LaunchedEffect(toast) {
        Toast.makeText(context, toast.message, duration).show()
    }
}
