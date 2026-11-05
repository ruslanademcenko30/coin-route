package com.Huslehi3.Topzom.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.domain.model.RunResult
import com.Huslehi3.Topzom.domain.repository.GameRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val gameRepository: GameRepository,
    private val result: RunResult
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val profile = gameRepository.loadProfile()
            _state.update {
                it.copy(
                    loading = false,
                    finalScore = result.score,
                    hits = result.hits,
                    bestStreak = result.bestStreak,
                    bestScore = profile.bestScore,
                    newBest = result.score > 0 && result.score >= profile.bestScore,
                    earnedTokens = result.score / GameConfig.TOKENS_PER_POINT,
                    routeNodes = (profile.routeProgress / GameConfig.ROUTE_SEGMENT)
                        .coerceAtMost(GameConfig.ROUTE_NODES)
                )
            }
            countUp()
        }
    }

    private suspend fun countUp() {
        val steps = 28
        repeat(steps) { index ->
            delay(COUNT_UP_MS / steps)
            _state.update { it.copy(displayedScore = result.score * (index + 1) / steps) }
        }
        _state.update { it.copy(displayedScore = result.score) }
    }

    private companion object {
        const val COUNT_UP_MS = 700L
    }
}
