package org.behemoth.dailydigest.platform

import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNUserNotificationCenter
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosNotificationManager : NotificationManager {
    private val notificationCenter = UNUserNotificationCenter.currentNotificationCenter()
    private val requestIdentifier = "daily_digest_reminder"

    override suspend fun requestPermission() = suspendCancellableCoroutine { continuation ->
        notificationCenter.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound
        ) { granted, error ->
            continuation.resume(Unit)
        }
    }

    override fun isPermissionGranted(): Boolean {
        var isGranted = false
        notificationCenter.getNotificationSettingsWithCompletionHandler { settings ->
            isGranted = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
        }
        return isGranted
    }

    override fun scheduleDailyNewsReminder(hour: Int, minute: Int) {
        val content = UNMutableNotificationContent().apply {
            setTitle("Daily News Digest")
            setBody("Start your day with the latest news!")
            setSound(UNNotificationSound.defaultSound)
        }

        // Create date components for the trigger
        val dateComponents = NSDateComponents().apply {
            setHour(hour.toLong())
            setMinute(minute.toLong())
        }

        // Create calendar trigger
        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents,
            true // Repeats daily
        )

        // Create request
        val request = UNNotificationRequest.requestWithIdentifier(
            requestIdentifier,
            content,
            trigger
        )

        // Schedule notification
        notificationCenter.addNotificationRequest(request, null)
    }

    override fun cancelDailyNewsReminder() {
        notificationCenter.removePendingNotificationRequestsWithIdentifiers(
            listOf(requestIdentifier)
        )
    }
}