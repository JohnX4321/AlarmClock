package com.tzapps.alarm.models

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel

class AlarmViewModel(@NonNull val app: Application): AndroidViewModel(app) {

    private val alarmRepo = AlarmRepository(app)

    fun insert(alarm: Alarm) = alarmRepo.insert(alarm)

    fun delete(alarm: Alarm) = alarmRepo.delete(alarm)

    fun update(alarm: Alarm) = alarmRepo.update(alarm)

}