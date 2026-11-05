package com.Huslehi3.Topzom.data.local

import android.content.Context
import android.content.SharedPreferences

class CoinRoutePreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun text(key: String, fallback: String): String = prefs.getString(key, fallback) ?: fallback

    fun putText(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun clearProgress() {
        prefs.edit()
            .remove(BEST_SCORE)
            .remove(BEST_STREAK)
            .remove(TOTAL_HITS)
            .remove(RUNS)
            .remove(TOKENS)
            .remove(LIFETIME_TOKENS)
            .remove(ROUTE_PROGRESS)
            .remove(UNLOCKED_ROUTES)
            .remove(UNLOCKED_TOKENS)
            .remove(SELECTED_TOKEN)
            .remove(CLAIMED_TIERS)
            .remove(DAILY_DATE)
            .remove(DAILY_GOALS)
            .apply()
    }

    companion object {
        private const val STORE = "coinroute_store"
        const val BEST_SCORE = "best_score"
        const val BEST_STREAK = "best_streak"
        const val TOTAL_HITS = "total_hits"
        const val RUNS = "runs"
        const val TOKENS = "tokens"
        const val LIFETIME_TOKENS = "lifetime_tokens"
        const val ROUTE_PROGRESS = "route_progress"
        const val UNLOCKED_ROUTES = "unlocked_routes"
        const val UNLOCKED_TOKENS = "unlocked_tokens"
        const val SELECTED_TOKEN = "selected_token"
        const val CLAIMED_TIERS = "claimed_tiers"
        const val DAILY_DATE = "daily_date"
        const val DAILY_GOALS = "daily_goals"
        const val SOUND_ON = "sound_on"
        const val HAPTICS_ON = "haptics_on"
        const val AIM_ASSIST = "aim_assist"
    }
}
