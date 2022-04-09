package com.tzapps.alarm.utils

import java.util.*

object DayUtils {

    fun toDay(day: Int): String {
       return when(day) {
            Calendar.SUNDAY->"Sunday"
           Calendar.MONDAY->"Monday"
           Calendar.TUESDAY->"Tuesday"
           Calendar.WEDNESDAY->"Wednesday"
           Calendar.THURSDAY->"Thursday"
           Calendar.FRIDAY->"Friday"
           Calendar.SATURDAY->"Saturday"
           else->"null"
        }
    }

}



