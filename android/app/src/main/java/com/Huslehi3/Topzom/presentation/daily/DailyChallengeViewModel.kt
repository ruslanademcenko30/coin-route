package com.Huslehi3.Topzom.presentation.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.core.config.DayProvider
import com.Huslehi3.Topzom.domain.usecase.ResolveDailyChallengeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DailyChallengeViewModel(
    private val resolveDaily: ResolveDailyChallengeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DailyChallengeUiState())
    val state: StateFlow<DailyChallengeUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val daily = resolveDaily.today(DayProvider.currentDayOfYear())
            _state.update {
                it.copy(
                    loading = false,
                    modifierKey = daily.modifier.key,
                    goals = daily.goals,
                    rewardTokens = daily.rewardTokens,
                    allDone = daily.allDone
                )
            }
        }
    }
}
