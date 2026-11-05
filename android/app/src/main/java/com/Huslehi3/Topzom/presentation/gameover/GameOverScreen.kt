package com.Huslehi3.Topzom.presentation.gameover

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
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
import com.Huslehi3.Topzom.core.ui.AppIcons
import com.Huslehi3.Topzom.core.ui.GlassCard
import com.Huslehi3.Topzom.core.ui.PrimaryButton
import com.Huslehi3.Topzom.core.ui.RouteNodeRow
import com.Huslehi3.Topzom.core.ui.ScreenBackground
import com.Huslehi3.Topzom.core.ui.SecondaryButton
import com.Huslehi3.Topzom.core.ui.StatCard
import com.Huslehi3.Topzom.core.ui.formatAmount
import com.Huslehi3.Topzom.core.ui.formatFraction
import com.Huslehi3.Topzom.domain.model.RunResult

private val GameOverOverlay = listOf(
    Color(0xE6081624),
    Color(0xCC102A43),
    Color(0xF20A1A2B)
)

@Composable
fun GameOverScreen(
    result: RunResult,
    roundId: Long,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(
        key = "gameover-" + roundId,
        factory = AppViewModelFactory(result = result)
    )
    val state by viewModel.state.collectAsStateWithLifecycle()

    GameOverContent(
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
private fun GameOverContent(
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }
    val rise by animateFloatAsState(
        targetValue = if (entered) 0f else 24f,
        animationSpec = tween(480),
        label = "gameOverRise"
    )
    val fade by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(480),
        label = "gameOverFade"
    )
    val bestPulse by animateFloatAsState(
        targetValue = if (state.newBest && entered) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium),
        label = "bestPulse"
    )

    val hitsLabel = stringResource(R.string.stat_hits)
    val streakLabel = stringResource(R.string.stat_best_streak)
    val routeLabelText = stringResource(R.string.stat_route)
    val cards = remember(state, hitsLabel, streakLabel, routeLabelText) {
        buildList {
            if (state.hits > 0) {
                add(Triple(state.hits.toString(), hitsLabel, Secondary))
            }
            if (state.bestStreak > 0) {
                add(Triple(state.bestStreak.toString(), streakLabel, Accent))
            }
            add(
                Triple(
                    formatFraction(state.routeNodes, state.totalNodes),
                    routeLabelText,
                    Primary
                )
            )
        }
    }

    ScreenBackground(overlay = GameOverOverlay, tealBlobAlpha = 0.14f, goldBlobAlpha = 0.18f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
                .offset(y = rise.dp)
                .alpha(fade),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(88.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = if (state.newBest) {
                    stringResource(R.string.gameover_title_best)
                } else {
                    stringResource(R.string.gameover_title)
                },
                style = MaterialTheme.typography.headlineMedium,
                color = if (state.newBest) Accent else TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = formatAmount(state.displayedScore),
                style = MaterialTheme.typography.displaySmall,
                color = Accent,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )

            Text(
                text = stringResource(R.string.final_score),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            if (state.newBest) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.new_best),
                    style = MaterialTheme.typography.titleMedium,
                    color = Secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.scale(bestPulse)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            if (cards.size >= 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    cards.forEach { card ->
                        StatCard(
                            value = card.first,
                            label = card.second,
                            valueColor = card.third,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            RouteNodeRow(
                total = state.totalNodes,
                reached = state.routeNodes,
                nodeSize = 34.dp,
                spriteSize = 20.dp
            )

            if (state.earnedTokens > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = Accent.copy(alpha = 0.35f),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.sprite_4),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = stringResource(
                                R.string.reward_tokens,
                                formatAmount(state.earnedTokens)
                            ),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                icon = AppIcons.Refresh,
                height = 60.dp,
                gradient = listOf(Primary, Secondary),
                modifier = Modifier.fillMaxWidth()
            )

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
}
