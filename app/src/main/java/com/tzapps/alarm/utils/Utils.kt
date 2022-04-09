package com.tzapps.alarm.utils

import android.os.Build

object Utils {

    fun isAboveOreo() = Build.VERSION.SDK_INT>=Build.VERSION_CODES.O

    fun isAboveS() = Build.VERSION.SDK_INT>=Build.VERSION_CODES.S

}