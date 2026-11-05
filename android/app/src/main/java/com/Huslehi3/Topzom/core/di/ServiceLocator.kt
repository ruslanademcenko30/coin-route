package com.Huslehi3.Topzom.core.di

import android.content.Context
import com.Huslehi3.Topzom.data.local.CoinRoutePreferences
import com.Huslehi3.Topzom.data.repository.GameRepositoryImpl
import com.Huslehi3.Topzom.data.repository.SettingsRepositoryImpl
import com.Huslehi3.Topzom.domain.repository.GameRepository
import com.Huslehi3.Topzom.domain.repository.SettingsRepository
import com.Huslehi3.Topzom.domain.usecase.AdvanceRoundUseCase
import com.Huslehi3.Topzom.domain.usecase.ClaimRewardUseCase
import com.Huslehi3.Topzom.domain.usecase.LaunchCoinUseCase
import com.Huslehi3.Topzom.domain.usecase.ResolveDailyChallengeUseCase

object ServiceLocator {

    private var preferences: CoinRoutePreferences? = null

    val gameRepository: GameRepository by lazy { GameRepositoryImpl(requirePreferences()) }

    val settingsRepository: SettingsRepository by lazy { SettingsRepositoryImpl(requirePreferences()) }

    val advanceRound: AdvanceRoundUseCase by lazy { AdvanceRoundUseCase(gameRepository) }

    val claimReward: ClaimRewardUseCase by lazy { ClaimRewardUseCase(gameRepository) }

    val resolveDaily: ResolveDailyChallengeUseCase by lazy { ResolveDailyChallengeUseCase(gameRepository) }

    val launchCoin: LaunchCoinUseCase by lazy { LaunchCoinUseCase() }

    fun init(context: Context) {
        if (preferences == null) {
            preferences = CoinRoutePreferences(context.applicationContext)
        }
    }

    private fun requirePreferences(): CoinRoutePreferences =
        preferences ?: error("ServiceLocator is not initialised")
}
