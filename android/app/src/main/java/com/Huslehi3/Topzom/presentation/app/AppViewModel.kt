package com.Huslehi3.Topzom.presentation.app

import androidx.lifecycle.ViewModel
import com.Huslehi3.Topzom.core.navigation.Screen
import com.Huslehi3.Topzom.domain.model.RunResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppUiState(
    val screen: Screen = Screen.Splash,
    val canGoBack: Boolean = false
)

class AppViewModel : ViewModel() {

    private val backStack = ArrayDeque<Screen>()
    private var roundCounter = 0L

    private val _state = MutableStateFlow(AppUiState())
    val state: StateFlow<AppUiState> = _state.asStateFlow()

    fun openMenu() {
        backStack.clear()
        _state.update { AppUiState(screen = Screen.Menu, canGoBack = false) }
    }

    fun navigate(target: Screen) {
        val current = _state.value.screen
        if (current != Screen.Splash) {
            backStack.addLast(current)
        }
        _state.update { AppUiState(screen = target, canGoBack = backStack.isNotEmpty()) }
    }

    fun startRound(challenge: Boolean) {
        roundCounter += 1
        navigate(Screen.Game(challenge = challenge, roundId = roundCounter))
    }

    fun replayRound(challenge: Boolean) {
        roundCounter += 1
        _state.update {
            AppUiState(
                screen = Screen.Game(challenge = challenge, roundId = roundCounter),
                canGoBack = backStack.isNotEmpty()
            )
        }
    }

    fun finishRound(result: RunResult) {
        _state.update {
            AppUiState(
                screen = Screen.GameOver(result = result, roundId = roundCounter),
                canGoBack = backStack.isNotEmpty()
            )
        }
    }

    fun back(): Boolean {
        val previous = backStack.removeLastOrNull() ?: return false
        _state.update { AppUiState(screen = previous, canGoBack = backStack.isNotEmpty()) }
        return true
    }
}
