package com.Huslehi3.Topzom.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.Huslehi3.Topzom.R

object AppIcons {
    val PlayArrow: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_play_arrow)

    val Refresh: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_refresh)

    val Settings: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_settings)

    val Lock: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_lock)

    val Done: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_done)

    val CheckCircle: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_check_circle)

    val Star: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_star)

    val ArrowBack: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_arrow_back)

    val ArrowForward: ImageVector
        @Composable get() = ImageVector.vectorResource(R.drawable.ic_arrow_forward)
}
