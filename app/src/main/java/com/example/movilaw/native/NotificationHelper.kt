package com.example.movilaw.native

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val channelId = "movilaw_updates_channel"
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Actualizaciones de Legislación",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Alertas sobre modificaciones normativas y nuevos artículos"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showLawUpdateNotification(lawTitle: String, changeDescription: String) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Modificación: $lawTitle")
            .setContentText(changeDescription)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val uniqueNotificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(uniqueNotificationId, notification)
    }
}