package com.Huslehi3.Topzom.core.ui

import kotlin.math.roundToInt

fun formatAmount(value: Int): String {
    val raw = value.toString()
    if (raw.length <= 3) return raw
    val builder = StringBuilder()
    raw.forEachIndexed { index, symbol ->
        if (index > 0 && (raw.length - index) % 3 == 0) {
            builder.append(' ')
        }
        builder.append(symbol)
    }
    return builder.toString()
}

fun formatMultiplier(value: Float): String {
    val scaled = (value * 10f).roundToInt()
    return (scaled / 10).toString() + "." + (scaled % 10).toString()
}

fun formatFraction(current: Int, total: Int): String = "$current/$total"
