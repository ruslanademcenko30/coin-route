package com.Huslehi3.Topzom.domain.model

enum class TokenKey { BRASS, MEDALLION, CRATE, MARKER, EMBLEM }

data class TokenSkin(
    val key: TokenKey,
    val unlockTokens: Int
)
