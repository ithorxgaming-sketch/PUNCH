package com.punch.welcome

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.media.AudioAttributes
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class GreetingService : Service(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        startAsForeground()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isTest = intent?.getBooleanExtra("test", false) == true
        val delayMs = if (isTest) 0L else Prefs.delay(this) * 1000L
        handler.postDelayed({ tts = TextToSpeech(this, this) }, delayMs)
        // Safety: never stay alive more than 60 seconds
        handler.postDelayed({ stopSelf() }, delayMs + 60_000L)
        return START_NOT_STICKY
    }

    override fun onInit(status: Int) {
        val engine = tts
        if (status != TextToSpeech.SUCCESS || engine == null) {
            stopSelf()
            return
        }
        val lang = Prefs.lang(this)
        val locale = if (lang == 2) Locale("hi", "IN") else Locale("en", "IN")
        val result = engine.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            engine.setLanguage(Locale.getDefault())
        }
        engine.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
        )
        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {}
            override fun onDone(utteranceId: String?) { stopSelf() }
            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) { stopSelf() }
        })
        val text = Greetings.build(Prefs.name(this), lang)
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "welcome")
    }

    private fun startAsForeground() {
        val channelId = "welcome"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Welcome message", NotificationManager.IMPORTANCE_LOW)
            )
        }
        val notification: Notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, channelId)
        } else {
            @Suppress("DEPRECATION") Notification.Builder(this)
        }.setContentTitle("Punch Welcome")
            .setContentText("Playing welcome message")
            .setSmallIcon(android.R.drawable.ic_lock_silent_mode_off)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(1, notification)
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
