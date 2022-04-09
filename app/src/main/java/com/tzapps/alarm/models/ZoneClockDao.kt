package com.tzapps.alarm.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ZoneClockDao {
    
    @Query("SELECT * FROM select_zones WHERE id=:id")
    fun getZone(id: String): ZoneClock
    
    @Query("SELECT * FROM select_zones ORDER BY name ASC")
    fun getAllZones(): LiveData<MutableList<ZoneClock>>
    
    @Delete
    fun deleteZone(zone: ZoneClock)
    
    @Insert
    fun insertZone(zone: ZoneClock)
    
}