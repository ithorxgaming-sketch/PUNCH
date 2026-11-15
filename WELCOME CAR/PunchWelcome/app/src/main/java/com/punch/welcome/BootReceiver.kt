package com.punch.welcome

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) return

        // Some head units send more than one boot broadcast. Play only once per 2 minutes.
        val now = System.currentTimeMillis()
        if (now - Prefs.lastPlayed(context) < 2 * 60 * 1000) return
        Prefs.markPlayed(context)

        val svc = Intent(context, GreetingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(svc)
        } else {
            context.startService(svc)
        }
    }
}
