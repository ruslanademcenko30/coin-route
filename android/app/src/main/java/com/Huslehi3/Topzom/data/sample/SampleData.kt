package com.Huslehi3.Topzom.data.sample

import com.Huslehi3.Topzom.domain.model.DailyGoal
import com.Huslehi3.Topzom.domain.model.DailyGoalKey
import com.Huslehi3.Topzom.domain.model.DailyModifier
import com.Huslehi3.Topzom.domain.model.DailyModifierKey
import com.Huslehi3.Topzom.domain.model.RewardTier
import com.Huslehi3.Topzom.domain.model.Route
import com.Huslehi3.Topzom.domain.model.RouteKey
import com.Huslehi3.Topzom.domain.model.TierKey
import com.Huslehi3.Topzom.domain.model.TokenKey
import com.Huslehi3.Topzom.domain.model.TokenSkin

object SampleData {

    val routes: List<Route> = listOf(
        Route(RouteKey.HARBOUR, unlockHits = 0, driftBoost = 1.0f),
        Route(RouteKey.CANYON, unlockHits = 60, driftBoost = 1.15f),
        Route(RouteKey.SKYLINE, unlockHits = 180, driftBoost = 1.3f)
    )

    val tokens: List<TokenSkin> = listOf(
        TokenSkin(TokenKey.BRASS, unlockTokens = 0),
        TokenSkin(TokenKey.MEDALLION, unlockTokens = 150),
        TokenSkin(TokenKey.CRATE, unlockTokens = 450),
        TokenSkin(TokenKey.MARKER, unlockTokens = 1100),
        TokenSkin(TokenKey.EMBLEM, unlockTokens = 2400)
    )

    val tiers: List<RewardTier> = listOf(
        RewardTier(TierKey.SCOUT, requiredTokens = 200, grant = 100),
        RewardTier(TierKey.TRADER, requiredTokens = 600, grant = 250),
        RewardTier(TierKey.CAPTAIN, requiredTokens = 1500, grant = 500),
        RewardTier(TierKey.ADMIRAL, requiredTokens = 3000, grant = 900),
        RewardTier(TierKey.LEGEND, requiredTokens = 6000, grant = 1500)
    )

    val modifiers: List<DailyModifier> = listOf(
        DailyModifier(DailyModifierKey.DOUBLE_DRIFT, speedFactor = 1.4f, scoreFactor = 2, bonusChanceFactor = 1.0f),
        DailyModifier(DailyModifierKey.GOLD_RUSH, speedFactor = 1.0f, scoreFactor = 1, bonusChanceFactor = 3.0f),
        DailyModifier(DailyModifierKey.STEADY_HAND, speedFactor = 0.8f, scoreFactor = 1, bonusChanceFactor = 1.0f),
        DailyModifier(DailyModifierKey.TIGHT_LANES, speedFactor = 1.2f, scoreFactor = 1, bonusChanceFactor = 1.5f),
        DailyModifier(DailyModifierKey.LUCKY_MARKERS, speedFactor = 1.1f, scoreFactor = 1, bonusChanceFactor = 2.4f),
        DailyModifier(DailyModifierKey.LONG_HAUL, speedFactor = 1.25f, scoreFactor = 2, bonusChanceFactor = 0.8f),
        DailyModifier(DailyModifierKey.NIGHT_RUN, speedFactor = 1.3f, scoreFactor = 2, bonusChanceFactor = 1.2f)
    )

    val dailyGoals: List<DailyGoal> = listOf(
        DailyGoal(DailyGoalKey.HITS, target = 12, done = false),
        DailyGoal(DailyGoalKey.STREAK, target = 3, done = false),
        DailyGoal(DailyGoalKey.SCORE, target = 1200, done = false)
    )

    const val DAILY_REWARD_TOKENS = 120

    fun routeOf(key: RouteKey): Route = routes.first { it.key == key }

    fun tokenOf(key: TokenKey): TokenSkin = tokens.first { it.key == key }

    fun tierOf(key: TierKey): RewardTier = tiers.first { it.key == key }
}
