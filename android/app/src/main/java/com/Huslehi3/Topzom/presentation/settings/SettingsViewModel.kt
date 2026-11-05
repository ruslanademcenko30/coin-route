package com.Huslehi3.Topzom.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Huslehi3.Topzom.domain.model.AppSettings
import com.Huslehi3.Topzom.domain.repository.GameRepository
import com.Huslehi3.Topzom.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val gameRepository: GameRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val settings = settingsRepository.load()
            _state.update {
                it.copy(
                    loading = false,
                    soundOn = settings.soundOn,
                    hapticsOn = settings.hapticsOn,
                    aimAssist = settings.aimAssist
                )
            }
        }
    }

    fun setSound(value: Boolean) = persist { it.copy(soundOn = value) }

    fun setHaptics(value: Boolean) = persist { it.copy(hapticsOn = value) }

    fun setAimAssist(value: Boolean) = persist { it.copy(aimAssist = value) }

    fun askReset() {
        _state.update { it.copy(confirmReset = true) }
    }

    fun dismissReset() {
        _state.update { it.copy(confirmReset = false) }
    }

    fun confirmReset() {
        viewModelScope.launch {
            gameRepository.resetProgress()
            _state.update { it.copy(confirmReset = false) }
        }
    }

    private fun persist(change: (SettingsUiState) -> SettingsUiState) {
        viewModelScope.launch {
            val updated = change(_state.value)
            _state.value = updated
            settingsRepository.save(
                AppSettings(
                    soundOn = updated.soundOn,
                    hapticsOn = updated.hapticsOn,
                    aimAssist = updated.aimAssist
                )
            )
        }
    }
}
