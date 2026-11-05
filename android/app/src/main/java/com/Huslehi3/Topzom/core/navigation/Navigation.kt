package com.Huslehi3.Topzom.core.navigation

import com.Huslehi3.Topzom.domain.model.RunResult

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data class Game(val challenge: Boolean, val roundId: Long) : Screen

    data class GameOver(val result: RunResult, val roundId: Long) : Screen

    data object Daily : Screen

    data object Progress : Screen

    data object Collection : Screen

    data object Settings : Screen
}
