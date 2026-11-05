package com.Huslehi3.Topzom.data.repository

import com.Huslehi3.Topzom.data.local.CoinRoutePreferences
import com.Huslehi3.Topzom.domain.model.AppSettings
import com.Huslehi3.Topzom.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val prefs: CoinRoutePreferences
) : SettingsRepository {

    override fun load(): AppSettings = AppSettings(
        soundOn = prefs.bool(CoinRoutePreferences.SOUND_ON, true),
        hapticsOn = prefs.bool(CoinRoutePreferences.HAPTICS_ON, true),
        aimAssist = prefs.bool(CoinRoutePreferences.AIM_ASSIST, false)
    )

    override fun save(settings: AppSettings) {
        prefs.putBool(CoinRoutePreferences.SOUND_ON, settings.soundOn)
        prefs.putBool(CoinRoutePreferences.HAPTICS_ON, settings.hapticsOn)
        prefs.putBool(CoinRoutePreferences.AIM_ASSIST, settings.aimAssist)
    }
}
