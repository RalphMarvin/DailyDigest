package org.behemoth.dailydigest.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

/**
 * A data class representing a toast message
 */
data class ToastMessage(
    val message: String,
    val duration: ToastDuration = ToastDuration.SHORT
)

/**
 * Enum representing toast duration
 */
enum class ToastDuration {
    SHORT, // Short duration (typically 2 seconds)
    LONG   // Long duration (typically 3.5 seconds)
}

/**
 * A utility class for managing toast messages
 */
class ToastManager {
    private val _currentToast = mutableStateOf<ToastMessage?>(null)
    val currentToast: MutableState<ToastMessage?> = _currentToast

    fun showToast(message: String, duration: ToastDuration = ToastDuration.SHORT) {
        _currentToast.value = ToastMessage(message, duration)
    }

    fun clearToast() {
        _currentToast.value = null
    }
}

/**
 * Composable function to handle toast messages
 */
@Composable
fun rememberToastManager(): ToastManager {
    return remember { ToastManager() }
}

/**
 * Composable function to display toast messages
 */
@Composable
fun ToastHost(
    toastManager: ToastManager,
    content: @Composable () -> Unit
) {
    val currentToast = toastManager.currentToast.value

    content()

    currentToast?.let { toast ->
        LaunchedEffect(toast) {
            val durationMillis = when (toast.duration) {
                ToastDuration.SHORT -> 2000L
                ToastDuration.LONG -> 3500L
            }
            delay(durationMillis)
            toastManager.clearToast()
        }

        // Platform-specific toast implementation will be added here
        PlatformToast(toast)
    }
}

/**
 * Platform-specific toast implementation
 */
@Composable
expect fun PlatformToast(toast: ToastMessage)
