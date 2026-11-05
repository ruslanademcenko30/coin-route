package com.Huslehi3.Topzom.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.Huslehi3.Topzom.R
import com.Huslehi3.Topzom.domain.model.DailyGoal
import com.Huslehi3.Topzom.domain.model.DailyGoalKey
import com.Huslehi3.Topzom.domain.model.DailyModifierKey
import com.Huslehi3.Topzom.domain.model.RouteKey
import com.Huslehi3.Topzom.domain.model.TierKey
import com.Huslehi3.Topzom.domain.model.TokenKey

@Composable
fun routeLabel(key: RouteKey): String = stringResource(
    when (key) {
        RouteKey.HARBOUR -> R.string.route_harbour
        RouteKey.CANYON -> R.string.route_canyon
        RouteKey.SKYLINE -> R.string.route_skyline
    }
)

@Composable
fun routeUnlockLabel(key: RouteKey): String = stringResource(
    when (key) {
        RouteKey.HARBOUR -> R.string.route_unlock_harbour
        RouteKey.CANYON -> R.string.route_unlock_canyon
        RouteKey.SKYLINE -> R.string.route_unlock_skyline
    }
)

@Composable
fun tokenLabel(key: TokenKey): String = stringResource(
    when (key) {
        TokenKey.BRASS -> R.string.token_brass
        TokenKey.MEDALLION -> R.string.token_medallion
        TokenKey.CRATE -> R.string.token_crate
        TokenKey.MARKER -> R.string.token_marker
        TokenKey.EMBLEM -> R.string.token_emblem
    }
)

@Composable
fun tierLabel(key: TierKey): String = stringResource(
    when (key) {
        TierKey.SCOUT -> R.string.tier_scout
        TierKey.TRADER -> R.string.tier_trader
        TierKey.CAPTAIN -> R.string.tier_captain
        TierKey.ADMIRAL -> R.string.tier_admiral
        TierKey.LEGEND -> R.string.tier_legend
    }
)

@Composable
fun modifierTitle(key: DailyModifierKey): String = stringResource(
    when (key) {
        DailyModifierKey.DOUBLE_DRIFT -> R.string.modifier_double_drift
        DailyModifierKey.GOLD_RUSH -> R.string.modifier_gold_rush
        DailyModifierKey.STEADY_HAND -> R.string.modifier_steady_hand
        DailyModifierKey.TIGHT_LANES -> R.string.modifier_tight_lanes
        DailyModifierKey.LUCKY_MARKERS -> R.string.modifier_lucky_markers
        DailyModifierKey.LONG_HAUL -> R.string.modifier_long_haul
        DailyModifierKey.NIGHT_RUN -> R.string.modifier_night_run
    }
)

@Composable
fun modifierBody(key: DailyModifierKey): String = stringResource(
    when (key) {
        DailyModifierKey.DOUBLE_DRIFT -> R.string.modifier_double_drift_body
        DailyModifierKey.GOLD_RUSH -> R.string.modifier_gold_rush_body
        DailyModifierKey.STEADY_HAND -> R.string.modifier_steady_hand_body
        DailyModifierKey.TIGHT_LANES -> R.string.modifier_tight_lanes_body
        DailyModifierKey.LUCKY_MARKERS -> R.string.modifier_lucky_markers_body
        DailyModifierKey.LONG_HAUL -> R.string.modifier_long_haul_body
        DailyModifierKey.NIGHT_RUN -> R.string.modifier_night_run_body
    }
)

@Composable
fun goalLabel(goal: DailyGoal): String = when (goal.key) {
    DailyGoalKey.HITS -> stringResource(R.string.goal_hits, goal.target)
    DailyGoalKey.STREAK -> stringResource(R.string.goal_streak, goal.target)
    DailyGoalKey.SCORE -> stringResource(R.string.goal_score, goal.target)
}
