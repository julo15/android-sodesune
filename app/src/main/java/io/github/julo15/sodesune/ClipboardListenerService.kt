package io.github.julo15.sodesune

import android.app.PendingIntent
import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import java.net.URLEncoder

class ClipboardListenerService : Service(), ClipboardManager.OnPrimaryClipChangedListener {

    private val manager: ClipboardManager by lazy {
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private val notificationHelper: NotificationHelper by lazy {
        NotificationHelper(this)
    }

    override fun onCreate() {
        super.onCreate()
        manager.addPrimaryClipChangedListener(this)

        val notification = notificationHelper.getForegroundNotification("Copy some Japanese text to the keyboard!")
                .build()
        startForeground(NOTIFICATION_ID_FOREGROUND, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.removePrimaryClipChangedListener(this)
    }

    override fun onPrimaryClipChanged() {
        if (manager.hasPrimaryClip()) {
            val item = manager.primaryClip.getItemAt(0)?.text.toString()

            if (JapaneseHelper.containsJapanese(item)) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getTranslationUrl(item)))
                val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

                val notification = notificationHelper.getNotification(item.toString())
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                notificationHelper.notify(NOTIFICATION_ID_DEFAULT, notification)
            } else {
                notificationHelper.cancel(NOTIFICATION_ID_DEFAULT)
            }
        }
    }

    private fun getTranslationUrl(text: String): String {
        val encoded: String = URLEncoder.encode(JapaneseHelper.stripYukiko(text), "UTF-8")
        return "https://translate.google.com/#ja/en/$encoded"
    }

    companion object {
        val NOTIFICATION_ID_DEFAULT: Int = 1
        val NOTIFICATION_ID_FOREGROUND: Int = 2

        fun newIntent(context: Context): Intent {
            return Intent(context, ClipboardListenerService::class.java)
        }
    }
}
