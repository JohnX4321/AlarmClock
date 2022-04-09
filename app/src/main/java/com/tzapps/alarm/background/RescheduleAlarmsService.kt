package com.tzapps.alarm.background

import android.content.Intent
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleService
import com.tzapps.alarm.models.AlarmRepository

class RescheduleAlarmsService: LifecycleService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val repo=AlarmRepository(application)
        repo.alarmsLiveData.observe(this
        ) { t ->
            for (a in t!!)
                if (a.isStarted) a.schedule(applicationContext)
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

}