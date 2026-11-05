package com.Huslehi3.Topzom.domain.repository

import com.Huslehi3.Topzom.domain.model.AppSettings

interface SettingsRepository {
    fun load(): AppSettings
    fun save(settings: AppSettings)
}
