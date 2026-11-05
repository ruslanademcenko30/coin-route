package com.Huslehi3.Topzom.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.core.di.AppViewModelFactory
import com.Huslehi3.Topzom.core.theme.Accent
import com.Huslehi3.Topzom.core.theme.GlassBorder
import com.Huslehi3.Topzom.core.theme.GlassFill
import com.Huslehi3.Topzom.core.theme.Primary
import com.Huslehi3.Topzom.core.theme.Secondary
import com.Huslehi3.Topzom.core.theme.TextMuted
import com.Huslehi3.Topzom.core.theme.TextPrimary
import com.Huslehi3.Topzom.core.theme.TextSecondary
import com.Huslehi3.Topzom.core.ui.AppIcons
import com.Huslehi3.Topzom.core.ui.GlassCard
import com.Huslehi3.Topzom.core.ui.PrimaryButton
import com.Huslehi3.Topzom.core.ui.ScreenBackground
import com.Huslehi3.Topzom.core.ui.SecondaryButton
import com.Huslehi3.Topzom.core.ui.StatCard
import com.Huslehi3.Topzom.core.ui.formatAmount
import com.Huslehi3.Topzom.core.ui.formatFraction
import com.Huslehi3.Topzom.core.ui.modifierTitle

private val MenuOverlay = listOf(
    Color(0xB3102A43),
    Color(0x8C0F3A52),
    Color(0xE60A1A2B)
)

@Composable
fun MenuScreen(
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    MenuContent(
        state = state,
        onPlay = onPlay,
        onDaily = onDaily,
        onProgress = onProgress,
        onCollection = onCollection,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDaily: () -> Unit,
    onProgress: () -> Unit,
    onCollection: () -> Unit,
    onSettings: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "menuLoops")
    val float by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heroFloat"
    )
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }
    val heroScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.9f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "heroScale"
    )
    val contentAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(420),
        label = "menuAlpha"
    )

    val bestLabel = stringResource(R.string.stat_best)
    val streakLabel = stringResource(R.string.stat_streak)
    val routesLabel = stringResource(R.string.stat_routes)
    val statCards = remember(state, bestLabel, streakLabel, routesLabel) {
        buildList {
            if (state.bestScore > 0) {
                add(Triple(formatAmount(state.bestScore), bestLabel, Accent))
            }
            if (state.bestStreak > 0) {
                add(Triple(state.bestStreak.toString(), streakLabel, Secondary))
            }
            add(
                Triple(
                    formatFraction(state.unlockedRoutes, state.totalRoutes),
                    routesLabel,
                    Primary
                )
            )
        }
    }

    ScreenBackground(overlay = MenuOverlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable(onClick = onSettings),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(GlassFill, RoundedCornerShape(14.dp))
                            .border(1.dp, GlassBorder, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = AppIcons.Settings,
                            contentDescription = stringResource(R.string.cd_settings),
                            tint = Secondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (state.tokens > 0) {
                    Row(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(GlassFill)
                            .border(1.dp, Accent.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_1),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatAmount(state.tokens),
                            style = MaterialTheme.typography.titleMedium,
                            color = Accent,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.label_tokens),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Box(
                modifier = Modifier
                    .size(168.dp)
                    .offset(y = float.dp)
                    .scale(heroScale)
                    .clip(CircleShape)
                    .background(TextPrimary.copy(alpha = 0.06f))
                    .border(1.dp, Accent.copy(alpha = 0.28f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(132.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contentAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.labelSmall,
                color = Secondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contentAlpha)
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (statCards.size >= 2) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(contentAlpha),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    statCards.forEach { card ->
                        StatCard(
                            value = card.first,
                            label = card.second,
                            valueColor = card.third,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .alpha(contentAlpha),
                borderColor = Accent.copy(alpha = 0.35f),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                onClick = onDaily
            ) {
                Row(
                    modifier = Modifier.fillMaxHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_5),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(44.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.daily_title),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (state.dailyAllDone) {
                                stringResource(R.string.daily_all_done)
                            } else {
                                modifierTitle(state.dailyModifier)
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary,
                            maxLines = 2
                        )
                    }
                    Icon(
                        imageVector = AppIcons.ArrowForward,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                icon = AppIcons.PlayArrow,
                height = 62.dp,
                gradient = listOf(Primary, Secondary),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.collection),
                    onClick = onCollection,
                    icon = AppIcons.Star,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.progress),
                    onClick = onProgress,
                    icon = AppIcons.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
