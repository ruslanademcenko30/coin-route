package com.Huslehi3.Topzom.domain.model

enum class TierKey { SCOUT, TRADER, CAPTAIN, ADMIRAL, LEGEND }

data class RewardTier(
    val key: TierKey,
    val requiredTokens: Int,
    val grant: Int
)
