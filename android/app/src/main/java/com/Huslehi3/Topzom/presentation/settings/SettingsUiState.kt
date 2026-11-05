package com.Huslehi3.Topzom.presentation.settings

data class SettingsUiState(
    val loading: Boolean = true,
    val soundOn: Boolean = true,
    val hapticsOn: Boolean = true,
    val aimAssist: Boolean = false,
    val confirmReset: Boolean = false
)
