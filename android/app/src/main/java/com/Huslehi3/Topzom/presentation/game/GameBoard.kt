package com.Huslehi3.Topzom.presentation.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.core.config.GameConfig
import com.Huslehi3.Topzom.core.theme.Accent
import com.Huslehi3.Topzom.core.theme.AccentDeep
import com.Huslehi3.Topzom.core.theme.Danger
import com.Huslehi3.Topzom.core.theme.Primary
import com.Huslehi3.Topzom.core.theme.Secondary
import com.Huslehi3.Topzom.core.theme.TextPrimary
import com.Huslehi3.Topzom.domain.model.GamePhase
import com.Huslehi3.Topzom.domain.model.TargetKind
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

@Composable
fun GameBoard(
    state: GameUiState,
    onMeasured: (Float, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val frame = (GameConfig.BOARD_PADDING_DP + GameConfig.BOARD_BORDER_DP).dp
        val available = if (maxWidth - 32.dp < GameConfig.BOARD_MAX_WIDTH_DP.dp) {
            maxWidth - 32.dp
        } else {
            GameConfig.BOARD_MAX_WIDTH_DP.dp
        }
        val laneWidth = floor(((available - frame * 2) / GameConfig.LANES).value)
        val innerWidth = laneWidth * GameConfig.LANES
        val boardWidth = innerWidth.dp + frame * 2
        val boardHeight = maxHeight - 12.dp
        val innerHeight = (boardHeight - frame * 2).value

        LaunchedEffect(innerWidth, innerHeight) {
            onMeasured(innerWidth, innerHeight)
        }

        Box(
            modifier = Modifier
                .size(width = boardWidth, height = boardHeight)
                .clip(RoundedCornerShape(22.dp))
                .background(TextPrimary.copy(alpha = 0.05f))
                .border(
                    width = GameConfig.BOARD_BORDER_DP.dp,
                    color = if (state.phase == GamePhase.MISS) {
                        Danger.copy(alpha = 0.6f)
                    } else {
                        Primary.copy(alpha = 0.4f)
                    },
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(frame)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clipToBounds()
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val dash = PathEffect.dashPathEffect(floatArrayOf(8f, 10f), 0f)
                    repeat(GameConfig.LANES) { lane ->
                        val y = size.height * (0.14f + 0.16f * lane) + 38.dp.toPx()
                        drawLine(
                            color = Secondary.copy(alpha = 0.18f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx(),
                            pathEffect = dash
                        )
                    }
                    val launcherY = size.height - GameConfig.LAUNCHER_INSET_DP.dp.toPx()
                    val radians = Math.toRadians(state.aimDegrees.toDouble())
                    val length = 96.dp.toPx()
                    drawLine(
                        color = Accent.copy(
                            alpha = if (state.phase == GamePhase.READY) 0.9f else 0.4f
                        ),
                        start = Offset(size.width / 2f, launcherY),
                        end = Offset(
                            size.width / 2f + (sin(radians) * length).toFloat(),
                            launcherY - (cos(radians) * length).toFloat()
                        ),
                        strokeWidth = 2.dp.toPx()
                    )
                    if (state.phase == GamePhase.HIT && state.effectProgress > 0f) {
                        drawCircle(
                            color = Accent.copy(alpha = 0.8f * (1f - state.effectProgress)),
                            radius = 48.dp.toPx() * state.effectProgress,
                            center = Offset(
                                state.effectX.dp.toPx(),
                                state.effectY.dp.toPx()
                            ),
                            style = Stroke(width = 3.dp.toPx())
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .size(width = (innerWidth * state.routeFill).dp, height = 3.dp)
                        .background(Accent.copy(alpha = 0.75f), RoundedCornerShape(2.dp))
                )

                state.targets.forEach { target ->
                    if (target.alive) {
                        Box(
                            modifier = Modifier
                                .offset(x = (target.x - 32f).dp, y = (target.y - 32f).dp)
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(TextPrimary.copy(alpha = 0.07f))
                                .border(
                                    width = 1.dp,
                                    color = if (target.kind == TargetKind.BONUS) {
                                        Accent.copy(alpha = 0.6f)
                                    } else {
                                        Secondary.copy(alpha = 0.3f)
                                    },
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(spriteOf(target.kind)),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(56.dp)
                            )
                        }
                    }
                }

                val coin = state.coin
                if (coin != null) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = (coin.x - 18f).dp, y = (coin.y - 18f).dp)
                            .size(36.dp)
                            .rotate(coin.rotation)
                    )
                }

                Box(
                    modifier = Modifier
                        .offset(
                            x = (innerWidth / 2f - 36f).dp,
                            y = (innerHeight - GameConfig.LAUNCHER_INSET_DP - 36f + state.recoil * 4f).dp
                        )
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(AccentDeep.copy(alpha = 0.2f))
                        .border(2.dp, Accent.copy(alpha = 0.55f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(34.dp)
                    )
                }

                if (state.phase == GamePhase.HIT && state.lastPoints > 0) {
                    Text(
                        text = stringResource(R.string.points_gain, state.lastPoints),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Accent,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .offset(
                                x = (state.effectX - 24f).dp,
                                y = (state.effectY - 40f - 28f * state.effectProgress).dp
                            )
                            .alpha(1f - state.effectProgress)
                    )
                }
            }
        }
    }
}

private fun spriteOf(kind: TargetKind): Int = when (kind) {
    TargetKind.MEDALLION -> R.drawable.sprite_2
    TargetKind.CRATE -> R.drawable.sprite_3
    TargetKind.BONUS -> R.drawable.sprite_4
}
