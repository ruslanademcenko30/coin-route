package com.Huslehi3.Topzom.presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.core.di.AppViewModelFactory
import com.Huslehi3.Topzom.core.theme.Accent
import com.Huslehi3.Topzom.core.theme.AccentDeep
import com.Huslehi3.Topzom.core.theme.BgBase
import com.Huslehi3.Topzom.core.theme.BgDeep
import com.Huslehi3.Topzom.core.theme.GlassBorder
import com.Huslehi3.Topzom.core.theme.GlassFill
import com.Huslehi3.Topzom.core.theme.Secondary
import com.Huslehi3.Topzom.core.theme.TextMuted
import com.Huslehi3.Topzom.core.theme.TextPrimary
import com.Huslehi3.Topzom.core.ui.AppIcons
import com.Huslehi3.Topzom.core.ui.PrimaryButton
import com.Huslehi3.Topzom.core.ui.ScreenBackground
import com.Huslehi3.Topzom.core.ui.StatCard
import com.Huslehi3.Topzom.core.ui.formatAmount
import com.Huslehi3.Topzom.core.ui.formatMultiplier
import com.Huslehi3.Topzom.core.ui.routeLabel
import com.Huslehi3.Topzom.domain.model.RunResult
import com.Huslehi3.Topzom.presentation.dialog.PauseDialog

private val GameOverlay = listOf(
    Color(0xCC081624),
    Color(0x99102A43),
    Color(0xE60A1A2B)
)

@Composable
fun GameScreen(
    challenge: Boolean,
    roundId: Long,
    onGameOver: (RunResult) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(
        key = "game-" + roundId,
        factory = AppViewModelFactory(challenge = challenge)
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showPause by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { result -> onGameOver(result) }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameContent(
        state = state,
        onLaunch = viewModel::launch,
        onPause = {
            showPause = true
            viewModel.pause()
        },
        onBoardMeasured = viewModel::onBoardMeasured
    )

    if (showPause) {
        PauseDialog(
            onResume = {
                showPause = false
                viewModel.resume()
            },
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameContent(
    state: GameUiState,
    onLaunch: () -> Unit,
    onPause: () -> Unit,
    onBoardMeasured: (Float, Float) -> Unit
) {
    val coinsDescription = stringResource(R.string.cd_coins_left, state.coinsLeft)
    val hitsLabel = stringResource(R.string.stat_hits)
    val streakLabel = stringResource(R.string.stat_streak)
    val bestLabel = stringResource(R.string.stat_best)
    val cards = remember(state.hits, state.streak, state.bestScore, hitsLabel, streakLabel, bestLabel) {
        buildList {
            if (state.hits > 0) {
                add(Triple(state.hits.toString(), hitsLabel, Secondary))
            }
            if (state.streak > 0) {
                add(Triple(state.streak.toString(), streakLabel, Accent))
            }
            if (state.bestScore > 0) {
                add(Triple(formatAmount(state.bestScore), bestLabel, AccentDeep))
            }
        }
    }

    ScreenBackground(overlay = GameOverlay, tealBlobAlpha = 0.16f, goldBlobAlpha = 0.1f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .background(BgDeep.copy(alpha = 0.55f))
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onPause),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(GlassFill, RoundedCornerShape(14.dp))
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.icon_pause),
                            contentDescription = stringResource(R.string.pause),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = routeLabel(state.routeKey),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                    Text(
                        text = stringResource(
                            R.string.lane_info,
                            state.lane,
                            formatMultiplier(state.multiplier)
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formatAmount(state.score),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Accent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                        }
                    )
                    Text(
                        text = stringResource(R.string.stat_score),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                GameBoard(
                    state = state,
                    onMeasured = onBoardMeasured,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = BgBase.copy(alpha = 0.92f),
                        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(
                            R.string.streak_value,
                            formatMultiplier(state.multiplier)
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = Secondary,
                        modifier = Modifier.semantics {
                            liveRegion = LiveRegionMode.Polite
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.semantics {
                            contentDescription = coinsDescription
                        }
                    ) {
                        repeat(GameConfig.COINS_PER_RUN) { index ->
                            Image(
                                painter = painterResource(R.drawable.sprite_1),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(20.dp)
                                    .alpha(if (index < state.coinsLeft) 1f else 0.22f)
                            )
                        }
                    }
                }

                if (cards.size >= 2) {
                    Spacer(modifier = Modifier.height(12.dp))
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.game_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                PrimaryButton(
                    text = stringResource(R.string.launch),
                    onClick = onLaunch,
                    icon = AppIcons.PlayArrow,
                    enabled = state.launchEnabled,
                    height = 60.dp,
                    gradient = listOf(Accent, AccentDeep),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
