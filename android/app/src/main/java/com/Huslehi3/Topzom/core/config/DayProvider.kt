package com.Huslehi3.Topzom.core.config

import java.util.Calendar

object DayProvider {

    fun currentDayOfYear(): Int = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
}
