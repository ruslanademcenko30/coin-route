package com.Huslehi3.Topzom.presentation.game

import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.domain.model.GamePhase
import com.Huslehi3.Topzom.domain.model.RouteKey
import com.Huslehi3.Topzom.domain.model.Target

data class CoinState(
    val x: Float,
    val y: Float,
    val rotation: Float
)

data class GameUiState(
    val boardReady: Boolean = false,
    val phase: GamePhase = GamePhase.READY,
    val challenge: Boolean = false,
    val routeKey: RouteKey = RouteKey.HARBOUR,
    val lane: Int = 1,
    val score: Int = 0,
    val bestScore: Int = 0,
    val hits: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val coinsLeft: Int = GameConfig.COINS_PER_RUN,
    val targets: List<Target> = emptyList(),
    val aimDegrees: Float = 0f,
    val coin: CoinState? = null,
    val recoil: Float = 0f,
    val effectProgress: Float = 0f,
    val effectX: Float = 0f,
    val effectY: Float = 0f,
    val lastPoints: Int = 0,
    val routeNodesReached: Int = 0,
    val routeFill: Float = 0f
) {
    val multiplier: Float
        get() = minOf(1f + GameConfig.STREAK_STEP * streak, GameConfig.MAX_MULTIPLIER)

    val launchEnabled: Boolean
        get() = boardReady && phase == GamePhase.READY && coinsLeft > 0
}
