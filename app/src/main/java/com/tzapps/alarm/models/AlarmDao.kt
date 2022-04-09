package com.tzapps.alarm.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDao {

    @Insert
    fun insert(alarm: Alarm)

    @Query("DELETE FROM alarm_table")
    fun deleteAll()

    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    fun getAlarms(): LiveData<MutableList<Alarm>>

    @Query("SELECT * FROM alarm_table WHERE alarmId=:alarmId")
    fun getAlarm(alarmId: Int): Alarm

    @Update
    fun update(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)

}