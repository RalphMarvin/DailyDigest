package org.behemoth.dailydigest.presentation.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

/**
 * A utility class for managing onboarding status
 */
class OnboardingManager(private val settings: Settings) {
    private val _hasCompletedOnboarding = mutableStateOf(loadOnboardingStatus())
    val hasCompletedOnboarding: MutableState<Boolean> = _hasCompletedOnboarding

    private fun loadOnboardingStatus(): Boolean {
        return settings.get(ONBOARDING_COMPLETED_KEY, false)
    }

    fun completeOnboarding() {
        _hasCompletedOnboarding.value = true
        settings.set(ONBOARDING_COMPLETED_KEY, true)
    }

    companion object {
        private const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"
    }
}

/**
 * Composable function to handle onboarding status
 */
@Composable
fun rememberOnboardingManager(settings: Settings): OnboardingManager {
    return remember { OnboardingManager(settings) }
}
