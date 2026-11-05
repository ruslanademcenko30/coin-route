package com.Huslehi3.Topzom.domain.model

enum class TargetKind { MEDALLION, CRATE, BONUS }

data class Target(
    val id: Int,
    val lane: Int,
    val kind: TargetKind,
    val x: Float,
    val y: Float,
    val speed: Float,
    val alive: Boolean
)
