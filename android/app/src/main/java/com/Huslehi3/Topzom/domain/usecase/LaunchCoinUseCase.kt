package com.Huslehi3.Topzom.domain.usecase

import com.Huslehi3.Topzom.domain.model.Target
import kotlin.math.hypot

class LaunchCoinUseCase {

    operator fun invoke(
        coinX: Float,
        coinY: Float,
        targets: List<Target>,
        hitRadius: Float
    ): Target? {
        var closest: Target? = null
        var closestDistance = Float.MAX_VALUE
        targets.forEach { target ->
            if (target.alive) {
                val distance = hypot(target.x - coinX, target.y - coinY)
                if (distance < closestDistance) {
                    closestDistance = distance
                    closest = target
                }
            }
        }
        return if (closestDistance <= hitRadius) closest else null
    }
}
