package com.tzapps.alarm.listeners

import com.tzapps.alarm.models.Alarm

interface OnToggleAlarmListener {
    fun onToggle(alarm: Alarm)
}