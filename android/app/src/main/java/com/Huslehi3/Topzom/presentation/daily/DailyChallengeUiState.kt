package com.Huslehi3.Topzom.presentation.daily

import com.Huslehi3.Topzom.domain.model.DailyGoal
import com.Huslehi3.Topzom.domain.model.DailyModifierKey

data class DailyChallengeUiState(
    val loading: Boolean = true,
    val modifierKey: DailyModifierKey = DailyModifierKey.DOUBLE_DRIFT,
    val goals: List<DailyGoal> = emptyList(),
    val rewardTokens: Int = 0,
    val allDone: Boolean = false
)
