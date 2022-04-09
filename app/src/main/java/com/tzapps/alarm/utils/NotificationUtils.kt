package com.tzapps.alarm.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.tzapps.alarm.ClockApp
import com.tzapps.alarm.R
import com.tzapps.alarm.activities.RingActivity
import com.tzapps.alarm.background.AlarmReceiver
import com.tzapps.alarm.models.Alarm
import java.text.SimpleDateFormat
import java.util.*

object NotificationUtils {

    fun showRunningNotification(alarmId: Int): Notification {
        val fsIntent = Intent(ClockApp.context,RingActivity::class.java)
        val fsPendingIntent = PendingIntent.getActivity(ClockApp.context,0,fsIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val fT = SimpleDateFormat("hh:mm", Locale.getDefault()).format(System.currentTimeMillis())
        val builder = NotificationCompat.Builder(ClockApp.context,AppConst.CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText(fT)
            .setSmallIcon(R.drawable.ic_alarm)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
        if (Utils.isAboveOreo())
            builder.setFullScreenIntent(fsPendingIntent,true)
        else builder.setContentIntent(fsPendingIntent)
        return builder.build()
    }

    fun showPersistentNotification(alarmId: Int) {
        val nm = ClockApp.context.getSystemService(NotificationManager::class.java)
        val dismissIntent = Intent(ClockApp.context,AlarmReceiver::class.java)
        dismissIntent.putExtra(AppConst.KEY_ALARM_ID,alarmId)
        dismissIntent.action = AppConst.ACTION_DISMISS
        val dismissPI = PendingIntent.getBroadcast(ClockApp.context,alarmId,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        val sdf = SimpleDateFormat("hh:mm", Locale.getDefault())
        val snoozeLength = Prefs.getSnoozeTime()
        val fT = sdf.format(System.currentTimeMillis()+snoozeLength*60000)
        val builder = NotificationCompat.Builder(ClockApp.context,AppConst.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Alarm")
            .setContentText("Snooozing till $fT")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .addAction(R.drawable.ic_alarm_dismiss,"Dismiss",dismissPI)
        nm.notify(alarmId,builder.build())
    }

    fun deliverMissedNotification(alarm: Alarm) {
        val nm = ClockApp.context.getSystemService(NotificationManager::class.java)
        val sdf = SimpleDateFormat("hh:mm",Locale.getDefault())
        val fT = sdf.format("${alarm.hour}:${alarm.minute}")
        val builder = NotificationCompat.Builder(ClockApp.context,AppConst.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle("Alarm")
            .setContentText("Alarm missed: $fT")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
        nm.notify(alarm.alarmId,builder.build())
    }

}