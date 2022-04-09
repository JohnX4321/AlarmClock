package com.tzapps.alarm.models

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel

class ZoneViewModel(@NonNull val app: Application): AndroidViewModel(app) {

    var zoneRepository = ZoneRepository(app)
    var zonesLiveData = zoneRepository.alarmsLiveData

    fun insert(zoneClock: ZoneClock) = zoneRepository.insert(zoneClock)

    fun delete(zoneClock: ZoneClock) = zoneRepository.delete(zoneClock)

}