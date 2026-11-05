package com.Huslehi3.Topzom.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Huslehi3.Topzom.core.di.AppViewModelFactory
import com.Huslehi3.Topzom.core.navigation.Screen
import com.Huslehi3.Topzom.core.theme.BgDeep
import com.Huslehi3.Topzom.presentation.collection.CollectionScreen
import com.Huslehi3.Topzom.presentation.daily.DailyChallengeScreen
import com.Huslehi3.Topzom.presentation.game.GameScreen
import com.Huslehi3.Topzom.presentation.gameover.GameOverScreen
import com.Huslehi3.Topzom.presentation.menu.MenuScreen
import com.Huslehi3.Topzom.presentation.progress.ProgressScreen
import com.Huslehi3.Topzom.presentation.settings.SettingsScreen
import com.Huslehi3.Topzom.presentation.splash.SplashScreen

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = AppViewModelFactory())
    val state by appViewModel.state.collectAsStateWithLifecycle()

    BackHandler(enabled = state.canGoBack) {
        appViewModel.back()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        AnimatedContent(
            targetState = state.screen,
            transitionSpec = {
                if (targetState is Screen.Game) {
                    (fadeIn(tween(320)) + slideInVertically { it / 12 })
                        .togetherWith(fadeOut(tween(240)))
                } else {
                    fadeIn(tween(420)).togetherWith(fadeOut(tween(320)))
                }
            },
            label = "screenTransition"
        ) { screen ->
            when (screen) {
                is Screen.Splash -> SplashScreen(onFinished = appViewModel::openMenu)

                is Screen.Menu -> MenuScreen(
                    onPlay = { appViewModel.startRound(challenge = false) },
                    onDaily = { appViewModel.navigate(Screen.Daily) },
                    onProgress = { appViewModel.navigate(Screen.Progress) },
                    onCollection = { appViewModel.navigate(Screen.Collection) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                is Screen.Game -> GameScreen(
                    challenge = screen.challenge,
                    roundId = screen.roundId,
                    onGameOver = appViewModel::finishRound,
                    onMenu = appViewModel::openMenu
                )

                is Screen.GameOver -> GameOverScreen(
                    result = screen.result,
                    roundId = screen.roundId,
                    onPlayAgain = { appViewModel.replayRound(screen.result.challenge) },
                    onMenu = appViewModel::openMenu
                )

                is Screen.Daily -> DailyChallengeScreen(
                    onPlay = { appViewModel.startRound(challenge = true) },
                    onMenu = appViewModel::openMenu,
                    onBack = { appViewModel.back() }
                )

                is Screen.Progress -> ProgressScreen(
                    onMenu = appViewModel::openMenu,
                    onBack = { appViewModel.back() }
                )

                is Screen.Collection -> CollectionScreen(
                    onMenu = appViewModel::openMenu,
                    onBack = { appViewModel.back() }
                )

                is Screen.Settings -> SettingsScreen(
                    onMenu = appViewModel::openMenu,
                    onBack = { appViewModel.back() }
                )
            }
        }
    }
}
