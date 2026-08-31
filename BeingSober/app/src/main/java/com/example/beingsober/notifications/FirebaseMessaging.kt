package com.example.beingsober.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.beingsober.MainActivity
import com.example.beingsober.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessaging : FirebaseMessagingService() {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title =
            message.notification?.title
                ?: message.data["title"]
                ?: "Being Sober"

        val body =
            message.notification?.body
                ?: message.data["body"]
                ?: "Take a moment for yourself today."

        Log.d(
            "FCM",
            "Notification received: $title"
        )

        Log.d(
            "FCM",
            "Data received: ${message.data}"
        )

        showNotification(
            title = title,
            body = body
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(
            "FCM",
            "New FCM Token: $token"
        )

        // Later we can send this token to our backend.
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(
        title: String,
        body: String
    ) {

        val channelId =
            "being_sober_notifications"

        // --------------------------------
        // Notification Channel
        // --------------------------------

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =
                NotificationChannel(
                    channelId,
                    "Being Sober Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {

                    description =
                        "Recovery reminders and insights from Being Sober"
                }

            val notificationManager =
                getSystemService(
                    NotificationManager::class.java
                )

            notificationManager
                .createNotificationChannel(channel)
        }

        // --------------------------------
        // Open app when tapped
        // --------------------------------

        val intent =
            Intent(
                this,
                MainActivity::class.java
            ).apply {

                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        // --------------------------------
        // Build notification
        // --------------------------------

        val notification =
            NotificationCompat
                .Builder(
                    this,
                    channelId
                )
                .setSmallIcon(
                    R.drawable.ic_launcher_foreground
                )
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(
                    NotificationCompat.PRIORITY_HIGH
                )
                .setAutoCancel(true)
                .setContentIntent(
                    pendingIntent
                )
                .build()

        // --------------------------------
        // Display notification
        // --------------------------------

        NotificationManagerCompat
            .from(this)
            .notify(
                System.currentTimeMillis().toInt(),
                notification
            )
    }
}