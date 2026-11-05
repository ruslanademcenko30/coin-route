package com.Huslehi3.Topzom.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.Huslehi3.Topzom.core.theme.BgDeep
import com.Huslehi3.Topzom.core.theme.Danger
import com.Huslehi3.Topzom.core.theme.Secondary
import com.Huslehi3.Topzom.core.theme.TextMuted
import com.Huslehi3.Topzom.core.theme.TextPrimary
import com.Huslehi3.Topzom.core.theme.TextSecondary
import com.Huslehi3.Topzom.core.ui.GlassCard
import com.Huslehi3.Topzom.core.ui.ScreenScaffold
import com.Huslehi3.Topzom.core.ui.SecondaryButton
import com.Huslehi3.Topzom.presentation.dialog.ConfirmDialog

private val SettingsOverlay = listOf(
    Color(0xD9081624),
    Color(0x99102A43),
    Color(0xE60A1A2B)
)

@Composable
fun SettingsScreen(
    onMenu: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory())
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenScaffold(
        title = stringResource(R.string.settings),
        onBack = onBack,
        overlay = SettingsOverlay
    ) {
        SwitchRow(
            label = stringResource(R.string.setting_sound),
            checked = state.soundOn,
            onCheckedChange = viewModel::setSound
        )
        Spacer(modifier = Modifier.height(10.dp))
        SwitchRow(
            label = stringResource(R.string.setting_haptics),
            checked = state.hapticsOn,
            onCheckedChange = viewModel::setHaptics
        )
        Spacer(modifier = Modifier.height(10.dp))
        SwitchRow(
            label = stringResource(R.string.setting_aim_assist),
            checked = state.aimAssist,
            onCheckedChange = viewModel::setAimAssist
        )

        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Danger.copy(alpha = 0.35f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.setting_reset),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                TextButton(onClick = viewModel::askReset) {
                    Text(
                        text = stringResource(R.string.reset),
                        style = MaterialTheme.typography.titleMedium,
                        color = Danger
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = Secondary.copy(alpha = 0.25f),
            contentPadding = PaddingValues(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = stringResource(R.string.about_brand),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.about_version),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.virtual_notice),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Start
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        SecondaryButton(
            text = stringResource(R.string.menu),
            onClick = onMenu,
            iconRes = R.drawable.icon_home,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))
    }

    if (state.confirmReset) {
        ConfirmDialog(
            title = stringResource(R.string.setting_reset),
            body = stringResource(R.string.reset_body),
            confirmLabel = stringResource(R.string.reset),
            onConfirm = viewModel::confirmReset,
            onDismiss = viewModel::dismissReset
        )
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val onText = stringResource(R.string.state_on)
    val offText = stringResource(R.string.state_off)
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = if (checked) onText else offText },
        borderColor = if (checked) {
            Secondary.copy(alpha = 0.4f)
        } else {
            TextMuted.copy(alpha = 0.22f)
        },
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = if (checked) TextPrimary else TextSecondary
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BgDeep,
                    checkedTrackColor = Accent,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = BgDeep.copy(alpha = 0.6f)
                )
            )
        }
    }
}
