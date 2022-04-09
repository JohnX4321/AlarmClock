package com.tzapps.alarm.models

import android.app.Application

class AlarmRepository(val application: Application) {

    var alarmDao = AlarmDatabase.getDatabase(application).alarmDao();
    var alarmsLiveData=alarmDao.getAlarms()

    fun insert(alarm: Alarm) = AlarmDatabase.databaseWriteExecutor.execute { alarmDao.insert(alarm) }

    fun update(alarm: Alarm) = AlarmDatabase.databaseWriteExecutor.execute { alarmDao.update(alarm) }

    fun delete(alarm: Alarm) = AlarmDatabase.databaseWriteExecutor.execute { alarmDao.deleteAlarm(alarm) }


}