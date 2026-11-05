package com.Huslehi3.Topzom.domain.model

data class PlayerProfile(
    val bestScore: Int,
    val bestStreak: Int,
    val totalHits: Int,
    val runs: Int,
    val tokens: Int,
    val lifetimeTokens: Int,
    val routeProgress: Int,
    val unlockedRoutes: Set<RouteKey>,
    val unlockedTokens: Set<TokenKey>,
    val selectedToken: TokenKey,
    val claimedTiers: Set<TierKey>
)
