package com.tzapps.alarm.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "select_zones")
data class ZoneClock(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String
)