package org.behemoth.dailydigest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import org.behemoth.dailydigest.platform.AndroidUrlLauncher
import org.behemoth.dailydigest.platform.UrlLauncher

val LocalUrlLauncher = staticCompositionLocalOf<UrlLauncher> { error("No UrlLauncher provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompositionLocalProvider(
                LocalUrlLauncher provides AndroidUrlLauncher(this)
            ) {
                App()
            }
        }
    }

    companion object {
        fun openUrlInCustomTab(url: String, activity: ComponentActivity) {
            val customTabsIntent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            customTabsIntent.launchUrl(activity, Uri.parse(url))
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}