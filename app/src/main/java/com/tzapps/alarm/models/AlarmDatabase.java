package com.tzapps.alarm.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.tzapps.alarm.models.Alarm;
import com.tzapps.alarm.models.AlarmDao;

import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Alarm.class, ZoneClock.class},version = 1,exportSchema = false)
public abstract class AlarmDatabase extends RoomDatabase {

    public abstract AlarmDao alarmDao();

    public abstract ZoneClockDao zoneDao();

    private static volatile AlarmDatabase INSTANCE;
    private static final int NUM_OF_THREADS=4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUM_OF_THREADS);

    public static AlarmDatabase getDatabase(final Context context) {
        if (INSTANCE==null) {
            synchronized (AlarmDatabase.class) {
                INSTANCE= Room.databaseBuilder(context.getApplicationContext(),AlarmDatabase.class,"alarm_database").build();
            }
        }
        return INSTANCE;
    }

}
