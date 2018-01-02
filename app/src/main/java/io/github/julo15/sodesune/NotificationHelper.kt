package io.github.julo15.sodesune

import android.app.Notification
import android.app.Notification.*
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.os.Build

/**
 * Created by julianlo on 1/1/18.
 */
internal class NotificationHelper (context: Context) : ContextWrapper(context) {

    private val manager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel(DEFAULT_CHANNEL)
            manager.createNotificationChannel(NotificationChannel(DEFAULT_CHANNEL,
                    getString(R.string.notification_channel_default),
                    NotificationManager.IMPORTANCE_DEFAULT))

            manager.deleteNotificationChannel(FOREGROUND_CHANNEL)
            manager.createNotificationChannel(NotificationChannel(FOREGROUND_CHANNEL,
                    getString(R.string.notification_channel_foreground),
                    NotificationManager.IMPORTANCE_LOW))

        }
    }

    fun getNotification(text: String): Notification.Builder {
        return getNotification(DEFAULT_CHANNEL, text)
                .setContentTitle("Tap to translate!")
    }

    fun getForegroundNotification(text: String): Notification.Builder {
        return getNotification(FOREGROUND_CHANNEL, text)
    }

    private fun getNotification(channelId: String, text: String): Notification.Builder {
        return when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            true -> Notification.Builder(applicationContext, channelId)
            false -> Notification.Builder(applicationContext)
                    .setPriority(if (channelId == FOREGROUND_CHANNEL) PRIORITY_LOW else PRIORITY_DEFAULT) }
                .setContentText(text)
                .setSmallIcon(smallIcon)
    }

    fun notify(id: Int, notification: Notification.Builder) {
        manager.notify(id, notification.build())
    }

    fun cancel(id: Int) {
        manager.cancel(id)
    }

    private val smallIcon: Int
        get() = android.R.drawable.stat_notify_more

    companion object {
        val DEFAULT_CHANNEL = "default"
        val FOREGROUND_CHANNEL = "foreground"
    }
}