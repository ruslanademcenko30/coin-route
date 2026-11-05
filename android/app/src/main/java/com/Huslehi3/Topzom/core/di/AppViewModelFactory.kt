package com.Huslehi3.Topzom.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Huslehi3.Topzom.domain.model.RunResult
import com.Huslehi3.Topzom.presentation.app.AppViewModel
import com.Huslehi3.Topzom.presentation.collection.CollectionViewModel
import com.Huslehi3.Topzom.presentation.daily.DailyChallengeViewModel
import com.Huslehi3.Topzom.presentation.game.GameViewModel
import com.Huslehi3.Topzom.presentation.gameover.GameOverViewModel
import com.Huslehi3.Topzom.presentation.menu.MenuViewModel
import com.Huslehi3.Topzom.presentation.progress.ProgressViewModel
import com.Huslehi3.Topzom.presentation.settings.SettingsViewModel
import com.Huslehi3.Topzom.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val challenge: Boolean = false,
    private val result: RunResult = RunResult.EMPTY
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val game = ServiceLocator.gameRepository
        val settings = ServiceLocator.settingsRepository
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(game, ServiceLocator.resolveDaily)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(
                    gameRepository = game,
                    settingsRepository = settings,
                    advanceRound = ServiceLocator.advanceRound,
                    resolveDaily = ServiceLocator.resolveDaily,
                    launchCoin = ServiceLocator.launchCoin,
                    challenge = challenge
                )

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(game, result)

            modelClass.isAssignableFrom(DailyChallengeViewModel::class.java) ->
                DailyChallengeViewModel(ServiceLocator.resolveDaily)

            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(game, ServiceLocator.claimReward)

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(game)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(game, settings)

            else -> error("Unknown ViewModel " + modelClass.name)
        }
        return created as T
    }
}
