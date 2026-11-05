package com.Huslehi3.Topzom.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.Huslehi3.Topzom.core.theme.GlassBorder
import com.Huslehi3.Topzom.core.theme.GlassFill
import com.Huslehi3.Topzom.core.theme.Primary

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = GlassBorder,
    fillColor: Color = GlassFill,
    shape: Shape = RoundedCornerShape(20.dp),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val surface = modifier
        .shadow(
            elevation = 10.dp,
            shape = shape,
            ambientColor = Primary.copy(alpha = 0.35f),
            spotColor = Primary.copy(alpha = 0.35f)
        )
        .clip(shape)
        .background(fillColor)
        .border(1.dp, borderColor, shape)
    val interactive = if (onClick != null) surface.clickable(onClick = onClick) else surface
    Column(
        modifier = interactive.padding(contentPadding),
        content = content
    )
}
