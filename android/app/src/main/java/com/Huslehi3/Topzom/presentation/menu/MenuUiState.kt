package com.Huslehi3.Topzom.presentation.menu

import com.Huslehi3.Topzom.domain.model.DailyModifierKey

data class MenuUiState(
    val loading: Boolean = true,
    val tokens: Int = 0,
    val bestScore: Int = 0,
    val bestStreak: Int = 0,
    val unlockedRoutes: Int = 1,
    val totalRoutes: Int = 3,
    val dailyModifier: DailyModifierKey = DailyModifierKey.DOUBLE_DRIFT,
    val dailyAllDone: Boolean = false
)
