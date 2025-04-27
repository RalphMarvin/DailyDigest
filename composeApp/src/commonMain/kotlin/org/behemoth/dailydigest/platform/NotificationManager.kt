package org.behemoth.dailydigest.platform

interface NotificationManager {
    suspend fun requestPermission()
    fun isPermissionGranted(): Boolean
    fun scheduleDailyNewsReminder(hour: Int = 7, minute: Int = 30)
    fun cancelDailyNewsReminder()
}