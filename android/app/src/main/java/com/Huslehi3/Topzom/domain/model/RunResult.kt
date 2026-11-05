package com.Huslehi3.Topzom.domain.model

data class RunResult(
    val score: Int,
    val hits: Int,
    val bestStreak: Int,
    val challenge: Boolean,
    val routeProgress: Int
) {
    companion object {
        val EMPTY = RunResult(0, 0, 0, false, 0)
    }
}
