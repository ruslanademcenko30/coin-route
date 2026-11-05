package com.Huslehi3.Topzom.presentation.progress

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Huslehi3.Topzom.core.theme.Primary
import com.Huslehi3.Topzom.core.theme.Secondary
import com.Huslehi3.Topzom.core.theme.TextMuted
import com.Huslehi3.Topzom.core.theme.TextPrimary
import com.Huslehi3.Topzom.core.theme.TextSecondary
import com.Huslehi3.Topzom.core.ui.AppIcons
import com.Huslehi3.Topzom.core.ui.GlassCard
import com.Huslehi3.Topzom.core.ui.PrimaryButton
import com.Huslehi3.Topzom.core.ui.RouteNodeRow
import com.Huslehi3.Topzom.core.ui.ScreenScaffold
import com.Huslehi3.Topzom.core.ui.SecondaryButton
import com.Huslehi3.Topzom.core.ui.StatCard
import com.Huslehi3.Topzom.core.ui.formatAmount
import com.Huslehi3.Topzom.core.ui.tierLabel

private val ProgressOverlay = listOf(
    Color(0xD9081624),
    Color(0x99102A43),
    Color(0xE60A1A2B)
)

@Composable
fun ProgressScreen(
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.progress),
        onBack = onBack,
        overlay = ProgressOverlay
    ) {
        if (state.runs == 0) {
            Spacer(modifier = Modifier.height(24.dp))
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = Secondary.copy(alpha = 0.3f),
                contentPadding = PaddingValues(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.progress_empty_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.progress_empty_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        } else {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    value = formatAmount(state.bestScore),
                    label = stringResource(R.string.stat_best),
                    valueColor = Accent,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = formatAmount(state.totalHits),
                    label = stringResource(R.string.stat_hits),
                    valueColor = Secondary,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    value = state.bestStreak.toString(),
                    label = stringResource(R.string.stat_best_streak),
                    valueColor = Primary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = formatAmount(state.runs),
                    label = stringResource(R.string.stat_runs),
                    valueColor = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RouteNodeRow(
                total = state.totalNodes,
                reached = state.routeNodes,
                nodeSize = 40.dp,
                spriteSize = 22.dp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.rewards_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.tiers, key = { tier -> tier.key.name }) { tier ->
                    TierCard(tier = tier, onClaim = { viewModel.claim(tier.key) })
                }
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
private fun TierCard(tier: TierUi, onClaim: () -> Unit) {
    val claimedText = stringResource(R.string.state_claimed)
    val readyText = stringResource(R.string.state_ready)
    val lockedText = stringResource(R.string.state_locked)
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = when {
                    tier.claimed -> claimedText
                    tier.ready -> readyText
                    else -> lockedText
                }
            },
        borderColor = when {
            tier.claimed -> Accent.copy(alpha = 0.45f)
            tier.ready -> Secondary.copy(alpha = 0.5f)
            else -> TextMuted.copy(alpha = 0.22f)
        },
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tierLabel(tier.key),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.tier_requirement,
                        formatAmount(tier.requiredTokens),
                        formatAmount(tier.grant)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(TextMuted.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(tier.progress)
                            .height(4.dp)
                            .background(Secondary, RoundedCornerShape(2.dp))
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            when {
                tier.claimed -> Icon(
                    imageVector = AppIcons.CheckCircle,
                    contentDescription = null,
                    tint = Accent,
                    modifier = Modifier.size(26.dp)
                )

                tier.ready -> PrimaryButton(
                    text = stringResource(R.string.claim),
                    onClick = onClaim,
                    height = 48.dp,
                    gradient = listOf(Secondary, Primary),
                    modifier = Modifier.width(110.dp)
                )

                else -> Icon(
                    imageVector = AppIcons.Lock,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}
