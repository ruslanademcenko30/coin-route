package com.Huslehi3.Topzom.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CoinRouteColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = TextPrimary,
    primaryContainer = PrimaryDeep,
    onPrimaryContainer = TextPrimary,
    secondary = Secondary,
    onSecondary = BgDeep,
    secondaryContainer = SurfaceElevated,
    onSecondaryContainer = TextPrimary,
    tertiary = Accent,
    onTertiary = BgDeep,
    tertiaryContainer = AccentDeep,
    onTertiaryContainer = BgDeep,
    background = BgDeep,
    onBackground = TextPrimary,
    surface = BgBase,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceGlass,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorder,
    error = Danger,
    onError = TextPrimary
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoinRouteColorScheme,
        typography = AppTypography,
        content = content
    )
}
