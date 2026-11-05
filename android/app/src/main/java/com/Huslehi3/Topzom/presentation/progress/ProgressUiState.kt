package com.Huslehi3.Topzom.presentation.progress

import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.domain.model.TierKey

data class TierUi(
    val key: TierKey,
    val requiredTokens: Int,
    val grant: Int,
    val progress: Float,
    val claimed: Boolean,
    val ready: Boolean
)

data class ProgressUiState(
    val loading: Boolean = true,
    val bestScore: Int = 0,
    val totalHits: Int = 0,
    val bestStreak: Int = 0,
    val runs: Int = 0,
    val tokens: Int = 0,
    val routeNodes: Int = 0,
    val totalNodes: Int = GameConfig.ROUTE_NODES,
    val tiers: List<TierUi> = emptyList()
)
