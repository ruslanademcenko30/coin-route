package com.Huslehi3.Topzom.domain.usecase

import com.Huslehi3.Topzom.data.sample.SampleData
import com.Huslehi3.Topzom.domain.model.PlayerProfile
import com.Huslehi3.Topzom.domain.model.TierKey
import com.Huslehi3.Topzom.domain.repository.GameRepository

class ClaimRewardUseCase(private val repository: GameRepository) {

    fun isReady(profile: PlayerProfile, key: TierKey): Boolean {
        val tier = SampleData.tierOf(key)
        return profile.lifetimeTokens >= tier.requiredTokens && !profile.claimedTiers.contains(key)
    }

    operator fun invoke(key: TierKey): PlayerProfile {
        val profile = repository.loadProfile()
        if (!isReady(profile, key)) return profile
        return repository.claimTier(key)
    }
}
