package org.behemoth.dailydigest.platform

object ApiKeyProvider {
    val apiKey: String
        get() = BuildConfig.NEWS_API_KEY
} 