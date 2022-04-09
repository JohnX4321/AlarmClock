package com.tzapps.alarm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.tzapps.alarm.utils.AppConst
import com.tzapps.alarm.utils.Prefs
import com.tzapps.alarm.utils.Utils

class ClockApp: Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext!!
        Prefs.init()
        if (Utils.isAboveOreo()) {
            val manager = getSystemService(NotificationManager::class.java)
            if (manager.getNotificationChannel(AppConst.CHANNEL_ID) == null) {
                val nc =
                    NotificationChannel(AppConst.CHANNEL_ID, "Alarm", NotificationManager.IMPORTANCE_HIGH)
                nc.enableLights(true)
                nc.setBypassDnd(true)
                manager.createNotificationChannel(nc)
            }
        }
    }

}