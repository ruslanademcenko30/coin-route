package com.Huslehi3.Topzom.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.core.di.AppViewModelFactory
import com.Huslehi3.Topzom.core.theme.Accent
import com.Huslehi3.Topzom.core.theme.Secondary
import com.Huslehi3.Topzom.core.theme.TextMuted
import com.Huslehi3.Topzom.core.theme.TextPrimary
import com.Huslehi3.Topzom.core.theme.TextSecondary
import com.Huslehi3.Topzom.core.ui.AppIcons
import com.Huslehi3.Topzom.core.ui.GlassCard
import com.Huslehi3.Topzom.core.ui.ScreenScaffold
import com.Huslehi3.Topzom.core.ui.SecondaryButton
import com.Huslehi3.Topzom.core.ui.formatAmount
import com.Huslehi3.Topzom.core.ui.routeLabel
import com.Huslehi3.Topzom.core.ui.routeUnlockLabel
import com.Huslehi3.Topzom.core.ui.tokenLabel
import com.Huslehi3.Topzom.domain.model.TokenKey

private val CollectionOverlay = listOf(
    Color(0xD9081624),
    Color(0xA60F3A52),
    Color(0xE60A1A2B)
)

@Composable
fun CollectionScreen(
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.collection),
        onBack = onBack,
        overlay = CollectionOverlay
    ) {
        Text(
            text = stringResource(R.string.section_routes),
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.routes, key = { route -> route.key.name }) { route ->
                GlassCard(
                    modifier = Modifier
                        .size(width = 172.dp, height = 198.dp)
                        .alpha(if (route.unlocked) 1f else 0.5f),
                    borderColor = if (route.unlocked) {
                        Accent.copy(alpha = 0.5f)
                    } else {
                        TextMuted.copy(alpha = 0.25f)
                    },
                    contentPadding = PaddingValues(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_4),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = routeLabel(route.key),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = routeUnlockLabel(route.key),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                    if (!route.unlocked) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Icon(
                            imageVector = AppIcons.Lock,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(R.string.section_tokens),
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.tokens, key = { token -> token.key.name }) { token ->
                TokenTile(token = token, onSelect = { viewModel.select(token.key) })
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        SecondaryButton(
            text = stringResource(R.string.menu),
            onClick = onMenu,
            iconRes = R.drawable.icon_home,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
private fun TokenTile(token: TokenUi, onSelect: () -> Unit) {
    val selectedText = stringResource(R.string.state_selected)
    val unlockedText = stringResource(R.string.state_unlocked)
    val lockedText = stringResource(R.string.state_locked)
    GlassCard(
        modifier = Modifier
            .height(118.dp)
            .semantics {
                stateDescription = when {
                    token.selected -> selectedText
                    token.unlocked -> unlockedText
                    else -> lockedText
                }
            },
        borderColor = when {
            token.selected -> Accent.copy(alpha = 0.8f)
            token.unlocked -> Secondary.copy(alpha = 0.4f)
            else -> TextMuted.copy(alpha = 0.22f)
        },
        contentPadding = PaddingValues(8.dp),
        onClick = onSelect
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (token.unlocked) {
                Image(
                    painter = painterResource(spriteOf(token.key)),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Box(
                    modifier = Modifier.size(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = AppIcons.Lock,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (token.unlocked) {
                    tokenLabel(token.key)
                } else {
                    stringResource(R.string.token_locked, formatAmount(token.unlockTokens))
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (token.unlocked) TextSecondary else TextMuted,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

private fun spriteOf(key: TokenKey): Int = when (key) {
    TokenKey.BRASS -> R.drawable.sprite_1
    TokenKey.MEDALLION -> R.drawable.sprite_2
    TokenKey.CRATE -> R.drawable.sprite_3
    TokenKey.MARKER -> R.drawable.sprite_4
    TokenKey.EMBLEM -> R.drawable.sprite_5
}
