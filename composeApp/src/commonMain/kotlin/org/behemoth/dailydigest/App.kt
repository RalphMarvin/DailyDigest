package org.behemoth.dailydigest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.behemoth.dailydigest.di.initKoin
import org.behemoth.dailydigest.presentation.ui.MainScreen
import org.behemoth.dailydigest.platform.ProvideUrlLauncher
import org.behemoth.dailydigest.presentation.ui.common.OnboardingManager
import org.behemoth.dailydigest.presentation.ui.welcome.WelcomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

// Initialize Koin
private val koinApp = initKoin()

@Composable
@Preview
fun App() {
    // Inject the onboarding manager from Koin
    val onboardingManager = koinInject<OnboardingManager>()

    // Get the current onboarding status
    val hasCompletedOnboarding by onboardingManager.hasCompletedOnboarding

    // Remember if we should show the main screen (either initially or after onboarding)
    val showMainScreen = remember { mutableStateOf(hasCompletedOnboarding) }

    ProvideUrlLauncher {
        if (showMainScreen.value) {
            MainScreen()
        } else {
            WelcomeScreen(
                onboardingManager = onboardingManager,
                onStartClick = {
                    // Update the state to show the main screen
                    showMainScreen.value = true
                }
            )
        }
    }
}
