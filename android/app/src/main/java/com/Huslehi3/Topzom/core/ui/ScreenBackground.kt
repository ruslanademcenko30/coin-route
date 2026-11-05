package com.Huslehi3.Topzom.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.core.theme.Accent
import com.Huslehi3.Topzom.core.theme.BgDeep
import com.Huslehi3.Topzom.core.theme.Primary

@Composable
fun ScreenBackground(
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    tealBlobAlpha: Float = 0.22f,
    goldBlobAlpha: Float = 0.14f,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgDeep)
    ) {
        Image(
            painter = painterResource(R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlay))
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Primary.copy(alpha = tealBlobAlpha), Color.Transparent),
                            center = Offset(size.width * 0.08f, size.height * 0.16f),
                            radius = size.minDimension * 0.62f
                        ),
                        radius = size.minDimension * 0.62f,
                        center = Offset(size.width * 0.08f, size.height * 0.16f)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Accent.copy(alpha = goldBlobAlpha), Color.Transparent),
                            center = Offset(size.width * 0.96f, size.height * 0.46f),
                            radius = size.minDimension * 0.5f
                        ),
                        radius = size.minDimension * 0.5f,
                        center = Offset(size.width * 0.96f, size.height * 0.46f)
                    )
                },
            content = content
        )
    }
}
