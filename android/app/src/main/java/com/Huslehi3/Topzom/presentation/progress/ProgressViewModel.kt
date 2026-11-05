package com.Huslehi3.Topzom.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.PlayerProfile
import com.Huslehi3.Topzom.domain.model.TierKey
import com.Huslehi3.Topzom.domain.repository.GameRepository
import com.Huslehi3.Topzom.domain.usecase.ClaimRewardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val gameRepository: GameRepository,
    private val claimReward: ClaimRewardUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = map(gameRepository.loadProfile())
        }
    }

    fun claim(key: TierKey) {
        viewModelScope.launch {
            _state.value = map(claimReward(key))
        }
    }

    private fun map(profile: PlayerProfile): ProgressUiState = ProgressUiState(
        loading = false,
        bestScore = profile.bestScore,
        totalHits = profile.totalHits,
        bestStreak = profile.bestStreak,
        runs = profile.runs,
        tokens = profile.tokens,
        routeNodes = (profile.routeProgress / GameConfig.ROUTE_SEGMENT)
            .coerceAtMost(GameConfig.ROUTE_NODES),
        tiers = SampleData.tiers.map { tier ->
            TierUi(
                key = tier.key,
                requiredTokens = tier.requiredTokens,
                grant = tier.grant,
                progress = (profile.lifetimeTokens.toFloat() / tier.requiredTokens)
                    .coerceIn(0f, 1f),
                claimed = profile.claimedTiers.contains(tier.key),
                ready = claimReward.isReady(profile, tier.key)
            )
        }
    )
}
