package org.behemoth.dailydigest.platform

actual object ApiKeyProvider {
    actual val apiKey: String
        get() = "replace_with_ios_api_key" // TODO: Fetch from Info.plist or environment
} 