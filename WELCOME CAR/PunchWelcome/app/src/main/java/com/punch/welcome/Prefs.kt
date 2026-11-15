package com.punch.welcome

import android.content.Context

object Prefs {
    private fun sp(c: Context) = c.getSharedPreferences("punch_welcome", Context.MODE_PRIVATE)

    fun name(c: Context): String = sp(c).getString("name", "Boss") ?: "Boss"
    fun lang(c: Context): Int = sp(c).getInt("lang", 0)          // 0 Hinglish, 1 English, 2 Hindi
    fun delay(c: Context): Int = sp(c).getInt("delay", 8)        // seconds
    fun lastPlayed(c: Context): Long = sp(c).getLong("last", 0L)

    fun save(c: Context, name: String, lang: Int, delay: Int) {
        sp(c).edit().putString("name", name).putInt("lang", lang).putInt("delay", delay).apply()
    }

    fun markPlayed(c: Context) {
        sp(c).edit().putLong("last", System.currentTimeMillis()).apply()
    }
}
