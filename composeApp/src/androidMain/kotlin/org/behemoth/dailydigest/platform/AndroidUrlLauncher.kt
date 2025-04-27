package org.behemoth.dailydigest.platform

import androidx.activity.ComponentActivity
import org.behemoth.dailydigest.MainActivity

class AndroidUrlLauncher(private val activity: ComponentActivity) : UrlLauncher {
    override fun openUrl(url: String) {
        MainActivity.openUrlInCustomTab(url, activity)
    }
}