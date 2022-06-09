package com.android.manasask.calendar

import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getTitleText(cDay: CalendarDay): String {
        val fullSdf = SimpleDateFormat("MMM, yyyy")

        return upperFirst(fullSdf.format(getDate(cDay)))
    }

    private fun getDate(cDay: CalendarDay): Date {
        val calendar = Calendar.getInstance()
        calendar.set(cDay.year, cDay.month - 1, cDay.day)
        return calendar.time
    }

    fun upperFirst(str: String) = str.substring(0, 1).uppercase() + str.substring(1)
}