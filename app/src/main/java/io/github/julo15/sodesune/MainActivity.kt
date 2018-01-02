package io.github.julo15.sodesune

import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        syncService()
        enableSwitch.setOnCheckedChangeListener { _, checked ->
            isEnabled = checked
        }
        enableSwitch.isChecked = isEnabled
    }

    private fun syncService() {
        val intent = ClipboardListenerService.newIntent(this)

        if (isEnabled) {
            when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                true -> startForegroundService(intent)
                false -> startService(intent)
            }
        } else {
            stopService(intent)
        }
    }

    private val prefs: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    private var isEnabled: Boolean
        get() = prefs.getBoolean(PREF_KEY_ENABLED, true)
        set(value) {
            prefs
                    .edit()
                    .putBoolean(PREF_KEY_ENABLED, value)
                    .apply()
            syncService()
        }

    companion object {
        const val PREF_KEY_ENABLED: String = "is_enabled"
    }
}
