package com.Huslehi3.Topzom.presentation.daily

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.Huslehi3.Topzom.core.ui.ScreenScaffold
import com.Huslehi3.Topzom.core.ui.SecondaryButton
import com.Huslehi3.Topzom.core.ui.formatAmount
import com.Huslehi3.Topzom.core.ui.goalLabel
import com.Huslehi3.Topzom.core.ui.modifierBody
import com.Huslehi3.Topzom.core.ui.modifierTitle
import com.Huslehi3.Topzom.domain.model.DailyGoal

private val DailyOverlay = listOf(
    Color(0xD9081624),
    Color(0xA60F3A52),
    Color(0xE60A1A2B)
)

@Composable
fun DailyChallengeScreen(
    onPlay: () -> Unit,
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: DailyChallengeViewModel = viewModel(factory = AppViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    ScreenScaffold(
        title = stringResource(R.string.daily_title),
        onBack = onBack,
        overlay = DailyOverlay
    ) {
        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(132.dp)
                .background(TextPrimary.copy(alpha = 0.06f), CircleShape)
                .border(1.dp, Accent.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(88.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = modifierTitle(state.modifierKey),
            style = MaterialTheme.typography.titleMedium,
            color = Accent,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (state.allDone) {
                stringResource(R.string.daily_all_done)
            } else {
                modifierBody(state.modifierKey)
            },
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(18.dp))

        state.goals.forEach { goal ->
            GoalRow(goal = goal)
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Secondary.copy(alpha = 0.3f),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(
                        R.string.daily_reward,
                        formatAmount(state.rewardTokens)
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            text = stringResource(R.string.play_challenge),
            onClick = onPlay,
            icon = AppIcons.PlayArrow,
            enabled = !state.allDone,
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

@Composable
private fun GoalRow(goal: DailyGoal) {
    val doneText = stringResource(R.string.state_done)
    val openText = stringResource(R.string.state_open)
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = if (goal.done) doneText else openText },
        borderColor = if (goal.done) {
            Secondary.copy(alpha = 0.45f)
        } else {
            TextMuted.copy(alpha = 0.25f)
        },
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = if (goal.done) AppIcons.Done else AppIcons.Lock,
                contentDescription = null,
                tint = if (goal.done) Secondary else TextMuted,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = goalLabel(goal),
                style = MaterialTheme.typography.bodyLarge,
                color = if (goal.done) TextPrimary else TextSecondary
            )
        }
    }
}
