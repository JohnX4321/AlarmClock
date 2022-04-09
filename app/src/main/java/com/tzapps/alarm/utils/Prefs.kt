package com.tzapps.alarm.utils

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import androidx.preference.PreferenceManager
import com.tzapps.alarm.ClockApp

object Prefs {

    private lateinit var preferences: SharedPreferences

    fun init() {
        preferences = PreferenceManager.getDefaultSharedPreferences(ClockApp.context)
    }


    fun getNightMode() = preferences.getBoolean(
        AppConst.KEY_NIGHT_MODE,true)

    fun setNightMode(value: Boolean) = preferences.edit().putBoolean(
        AppConst.KEY_NIGHT_MODE,value).apply()

    fun getSnoozeTime() = preferences.getInt(AppConst.KEY_SNOOZE,0)

    fun setSnoozeTime(value: Int) = preferences.edit().putInt(AppConst.KEY_SNOOZE,value).apply()

    fun getVibrationEnabled() = preferences.getBoolean(AppConst.KEY_VIBRATION,true)

    fun setVibrationEnabled(value: Boolean) = preferences.edit().putBoolean(AppConst.KEY_VIBRATION,value).apply()

    fun getRingtoneUri() = preferences.getString(AppConst.KEY_RINGTONE,RingtoneManager.getActualDefaultRingtoneUri(ClockApp.context,RingtoneManager.TYPE_ALARM).toString())

    fun setRingtoneUri(value: String) = preferences.edit().putString(AppConst.KEY_RINGTONE,value).apply()

    fun getTimeout() = preferences.getInt(AppConst.KEY_TIMEOUT,10)

    fun setTimeout(value: Int) = preferences.edit().putInt(AppConst.KEY_TIMEOUT,value).apply()

    fun getCrescendo() = preferences.getInt(AppConst.KEY_CRESCENDO,10)

    fun setCrescendo(value: Int) = preferences.edit().putInt(AppConst.KEY_CRESCENDO,value).apply()

    fun set24Hours(value: Boolean) = preferences.edit().putBoolean(AppConst.KEY_24_HOURS,value).apply()

    fun get24hours() = preferences.getBoolean(AppConst.KEY_24_HOURS,true)

}