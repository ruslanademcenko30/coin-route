package com.Huslehi3.Topzom.domain.repository

import com.Huslehi3.Topzom.domain.model.DailyState
import com.Huslehi3.Topzom.domain.model.PlayerProfile
import com.Huslehi3.Topzom.domain.model.TierKey
import com.Huslehi3.Topzom.domain.model.TokenKey

interface GameRepository {
    fun loadProfile(): PlayerProfile
    fun saveProfile(profile: PlayerProfile)
    fun selectToken(key: TokenKey): PlayerProfile
    fun claimTier(key: TierKey): PlayerProfile
    fun resetProgress(): PlayerProfile
    fun loadDaily(dayOfYear: Int): DailyState
    fun saveDaily(state: DailyState)
}
