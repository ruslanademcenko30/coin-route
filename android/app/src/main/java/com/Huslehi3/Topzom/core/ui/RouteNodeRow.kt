package com.Huslehi3.Topzom.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.core.theme.Accent
import com.Huslehi3.Topzom.core.theme.Primary
import com.Huslehi3.Topzom.core.theme.TextMuted

@Composable
fun RouteNodeRow(
    total: Int,
    reached: Int,
    modifier: Modifier = Modifier,
    nodeSize: Dp = 44.dp,
    spriteSize: Dp = 24.dp
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(total) { index ->
            val unlocked = index < reached
            Box(
                modifier = Modifier
                    .size(nodeSize)
                    .background(
                        color = if (unlocked) Accent.copy(alpha = 0.18f) else Primary.copy(alpha = 0.12f),
                        shape = CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = if (unlocked) Accent.copy(alpha = 0.7f) else TextMuted.copy(alpha = 0.35f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (unlocked) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(spriteSize)
                    )
                } else {
                    Icon(
                        imageVector = AppIcons.Lock,
                        contentDescription = null,
                        tint = TextMuted.copy(alpha = 0.7f),
                        modifier = Modifier.size(spriteSize * 0.7f)
                    )
                }
            }
            if (index < total - 1) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .weight(1f)
                        .background(
                            if (index < reached - 1) Accent.copy(alpha = 0.55f)
                            else TextMuted.copy(alpha = 0.25f)
                        )
                )
            }
        }
    }
}
