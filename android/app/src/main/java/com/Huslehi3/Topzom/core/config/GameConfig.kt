package com.Huslehi3.Topzom.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val TICK_MS = 16L
    const val AIM_SWEEP_MS = 1600L
    const val COIN_FLIGHT_MS = 520L
    const val TARGET_RESPAWN_MS = 450L
    const val HIT_FEEDBACK_MS = 420L
    const val MISS_FEEDBACK_MS = 320L
    const val FINISH_DELAY_MS = 400L

    const val LANES = 3
    const val COINS_PER_RUN = 3
    const val BASE_POINTS = 50
    const val MAX_MULTIPLIER = 4.0f
    const val STREAK_STEP = 0.25f
    const val BONUS_FACTOR = 3
    const val BONUS_CHANCE = 0.12f
    const val ROUTE_SEGMENT = 10
    const val ROUTE_NODES = 6
    const val MAX_LEVEL = 8

    const val BOARD_PADDING_DP = 8
    const val BOARD_BORDER_DP = 2
    const val BOARD_MAX_WIDTH_DP = 380
    const val HIT_RADIUS_DP = 28f
    const val AIM_ASSIST_RADIUS_DP = 34f
    const val TARGET_SIZE_DP = 56f
    const val LAUNCHER_INSET_DP = 46f
    const val MAX_AIM_DEGREES = 52f
    const val LANE_AREA_RATIO = 0.62f

    val LANE_SPEEDS_DP = floatArrayOf(48f, 62f, 78f)
    const val LEVEL_SPEED_STEP = 0.08f
    const val TOKENS_PER_POINT = 10
}
