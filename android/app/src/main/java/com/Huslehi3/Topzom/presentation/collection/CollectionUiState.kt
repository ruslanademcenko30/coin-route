package com.Huslehi3.Topzom.presentation.collection

import com.Huslehi3.Topzom.domain.model.RouteKey
import com.Huslehi3.Topzom.domain.model.TokenKey

data class RouteUi(
    val key: RouteKey,
    val unlocked: Boolean
)

data class TokenUi(
    val key: TokenKey,
    val unlocked: Boolean,
    val selected: Boolean,
    val unlockTokens: Int
)

data class CollectionUiState(
    val loading: Boolean = true,
    val routes: List<RouteUi> = emptyList(),
    val tokens: List<TokenUi> = emptyList()
)
