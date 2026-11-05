package com.Huslehi3.Topzom.domain.usecase

import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.PlayerProfile
import com.Huslehi3.Topzom.domain.model.RunResult
import com.Huslehi3.Topzom.domain.repository.GameRepository

data class RoundOutcome(
    val profile: PlayerProfile,
    val newBest: Boolean,
    val earnedTokens: Int,
    val unlockedRoute: Boolean,
    val unlockedToken: Boolean
)

class AdvanceRoundUseCase(private val repository: GameRepository) {

    operator fun invoke(result: RunResult): RoundOutcome {
        val current = repository.loadProfile()
        val earned = result.score / GameConfig.TOKENS_PER_POINT
        val totalHits = current.totalHits + result.hits
        val lifetime = current.lifetimeTokens + earned
        val routeProgress = (current.routeProgress + result.hits)
            .coerceAtMost(GameConfig.ROUTE_SEGMENT * GameConfig.ROUTE_NODES)

        val routes = current.unlockedRoutes + SampleData.routes
            .filter { totalHits >= it.unlockHits }
            .map { it.key }
            .toSet()
        val tokens = current.unlockedTokens + SampleData.tokens
            .filter { lifetime >= it.unlockTokens }
            .map { it.key }
            .toSet()

        val updated = current.copy(
            bestScore = maxOf(current.bestScore, result.score),
            bestStreak = maxOf(current.bestStreak, result.bestStreak),
            totalHits = totalHits,
            runs = current.runs + 1,
            tokens = current.tokens + earned,
            lifetimeTokens = lifetime,
            routeProgress = routeProgress,
            unlockedRoutes = routes,
            unlockedTokens = tokens
        )
        repository.saveProfile(updated)
        return RoundOutcome(
            profile = updated,
            newBest = result.score > current.bestScore && result.score > 0,
            earnedTokens = earned,
            unlockedRoute = routes.size > current.unlockedRoutes.size,
            unlockedToken = tokens.size > current.unlockedTokens.size
        )
    }
}
