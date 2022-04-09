package com.tzapps.alarm.models

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel

class AlarmsListViewModel(@NonNull val app: Application): AndroidViewModel(app) {

    var alarmRepository = AlarmRepository(app)
    var alarmsLiveData = alarmRepository.alarmsLiveData

    fun update(alarm: Alarm) = alarmRepository.update(alarm)

    fun delete(alarm: Alarm) = alarmRepository.delete(alarm)

}