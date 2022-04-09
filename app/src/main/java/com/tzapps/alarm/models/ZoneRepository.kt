package com.tzapps.alarm.models

import android.app.Application

class ZoneRepository(application: Application) {

    var zoneDao = AlarmDatabase.getDatabase(application).zoneDao();
    var alarmsLiveData=zoneDao.getAllZones()

    fun insert(zoneClock: ZoneClock) = AlarmDatabase.databaseWriteExecutor.execute { zoneDao.insertZone(zoneClock) }

    fun delete(zoneClock: ZoneClock) = AlarmDatabase.databaseWriteExecutor.execute { zoneDao.deleteZone(zoneClock) }

}