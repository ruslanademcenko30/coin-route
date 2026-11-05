package com.Huslehi3.Topzom.domain.model

enum class DailyModifierKey {
    DOUBLE_DRIFT,
    GOLD_RUSH,
    STEADY_HAND,
    TIGHT_LANES,
    LUCKY_MARKERS,
    LONG_HAUL,
    NIGHT_RUN
}

enum class DailyGoalKey { HITS, STREAK, SCORE }

data class DailyModifier(
    val key: DailyModifierKey,
    val speedFactor: Float,
    val scoreFactor: Int,
    val bonusChanceFactor: Float
)

data class DailyGoal(
    val key: DailyGoalKey,
    val target: Int,
    val done: Boolean
)

data class DailyState(
    val dayOfYear: Int,
    val modifier: DailyModifier,
    val goals: List<DailyGoal>,
    val rewardTokens: Int
) {
    val allDone: Boolean get() = goals.all { it.done }
}
