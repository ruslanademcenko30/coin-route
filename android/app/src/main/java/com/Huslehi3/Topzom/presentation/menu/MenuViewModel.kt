package com.Huslehi3.Topzom.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.core.config.DayProvider
import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.repository.GameRepository
import com.Huslehi3.Topzom.domain.usecase.ResolveDailyChallengeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MenuViewModel(
    private val gameRepository: GameRepository,
    private val resolveDaily: ResolveDailyChallengeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val profile = gameRepository.loadProfile()
            val daily = resolveDaily.today(DayProvider.currentDayOfYear())
            _state.update {
                it.copy(
                    loading = false,
                    tokens = profile.tokens,
                    bestScore = profile.bestScore,
                    bestStreak = profile.bestStreak,
                    unlockedRoutes = profile.unlockedRoutes.size,
                    totalRoutes = SampleData.routes.size,
                    dailyModifier = daily.modifier.key,
                    dailyAllDone = daily.allDone
                )
            }
        }
    }
}
