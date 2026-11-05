package com.Huslehi3.Topzom.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.PlayerProfile
import com.Huslehi3.Topzom.domain.model.TokenKey
import com.Huslehi3.Topzom.domain.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = map(gameRepository.loadProfile())
        }
    }

    fun select(key: TokenKey) {
        viewModelScope.launch {
            _state.value = map(gameRepository.selectToken(key))
        }
    }

    private fun map(profile: PlayerProfile): CollectionUiState = CollectionUiState(
        loading = false,
        routes = SampleData.routes.map { route ->
            RouteUi(
                key = route.key,
                unlocked = profile.unlockedRoutes.contains(route.key)
            )
        },
        tokens = SampleData.tokens.map { token ->
            TokenUi(
                key = token.key,
                unlocked = profile.unlockedTokens.contains(token.key),
                selected = profile.selectedToken == token.key,
                unlockTokens = token.unlockTokens
            )
        }
    )
}
