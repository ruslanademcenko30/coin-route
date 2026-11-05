package com.Huslehi3.Topzom.presentation.gameover

import com.Huslehi3.Topzom.core.config.GameConfig

data class GameOverUiState(
    val loading: Boolean = true,
    val finalScore: Int = 0,
    val displayedScore: Int = 0,
    val hits: Int = 0,
    val bestStreak: Int = 0,
    val bestScore: Int = 0,
    val newBest: Boolean = false,
    val earnedTokens: Int = 0,
    val routeNodes: Int = 0,
    val totalNodes: Int = GameConfig.ROUTE_NODES
)
