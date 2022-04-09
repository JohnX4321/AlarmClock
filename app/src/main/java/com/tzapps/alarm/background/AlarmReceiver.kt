package com.tzapps.alarm.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tzapps.alarm.ClockApp
import com.tzapps.alarm.models.AlarmDatabase
import com.tzapps.alarm.utils.AppConst.FRIDAY
import com.tzapps.alarm.utils.AppConst.MONDAY
import com.tzapps.alarm.utils.AppConst.RECURRING
import com.tzapps.alarm.utils.AppConst.SATURDAY
import com.tzapps.alarm.utils.AppConst.SUNDAY
import com.tzapps.alarm.utils.AppConst.THURSDAY
import com.tzapps.alarm.utils.AppConst.TITLE
import com.tzapps.alarm.utils.AppConst.TUESDAY
import com.tzapps.alarm.utils.AppConst.WEDNESDAY
import java.util.*

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(cont: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED==intent?.action) {
            Toast.makeText(cont,"Alarm Reboot",Toast.LENGTH_SHORT).show()
            startRescheduleAlarmsService(cont!!)
        } else {
            Toast.makeText(cont,"Alarm Received",Toast.LENGTH_SHORT).show()
            if (!intent?.getBooleanExtra(RECURRING,false)!!) startAlarmService(cont!!,intent)
            if (alarmIsToday(intent)) startAlarmService(cont!!,intent)
        }
    }

    private fun alarmIsToday(intent: Intent) : Boolean {
        val c=Calendar.getInstance()
        c.timeInMillis=System.currentTimeMillis()
        var today = c.get(Calendar.DAY_OF_WEEK)
        return when(today) {
            Calendar.MONDAY-> intent.getBooleanExtra(MONDAY,false)
            Calendar.TUESDAY->intent.getBooleanExtra(TUESDAY,false)
            Calendar.WEDNESDAY->intent.getBooleanExtra(WEDNESDAY,false)
            Calendar.THURSDAY->intent.getBooleanExtra(THURSDAY,false)
            Calendar.FRIDAY->intent.getBooleanExtra(FRIDAY,false)
            Calendar.SATURDAY->intent.getBooleanExtra(SATURDAY,false)
            Calendar.SUNDAY->intent.getBooleanExtra(SUNDAY,false)
            else->false
        }
    }

    private fun startAlarmService(cont: Context,intent: Intent) {
        val intentS = Intent(cont,AlarmService::class.java)
        intentS.putExtra(TITLE,intent.getStringExtra(TITLE))
        ContextCompat.startForegroundService(cont,intentS)
    }

    private fun startRescheduleAlarmsService(cont: Context) {
        ContextCompat.startForegroundService(cont,Intent(cont,RescheduleAlarmsService::class.java))
    }

    //optimize

    private fun getNextAlarmTime(alarmId: Int): Long {
        val alarm = AlarmDatabase.getDatabase(ClockApp.context).alarmDao().getAlarm(alarmId)
        if (!alarm.isRecurring) {
            return -1L
        }
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        if (today == Calendar.MONDAY) {
            return when {
                alarm.isTuesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isWednesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isThursday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isFriday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isSaturday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isSunday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
        } else if (today==Calendar.TUESDAY){
            return when {
                alarm.isWednesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isThursday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isFriday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isSaturday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isSunday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isMonday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
        } else if (today==Calendar.WEDNESDAY) {
            return when {
                alarm.isThursday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isFriday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isSaturday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isSunday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isMonday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isTuesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
         } else if (today==Calendar.THURSDAY) {
            return when {
                alarm.isFriday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isSaturday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isSunday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isMonday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isTuesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isWednesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
        } else if (today==Calendar.FRIDAY) {
            return when {
                alarm.isSaturday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isSunday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isMonday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isTuesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isWednesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isThursday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
        } else if (today==Calendar.SATURDAY) {
            return when {
                alarm.isSunday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isMonday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isTuesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isWednesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isThursday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isFriday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
        } else {
            return when {
                alarm.isMonday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,1)
                alarm.isTuesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,2)
                alarm.isWednesday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,3)
                alarm.isThursday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,4)
                alarm.isFriday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,5)
                alarm.isSaturday -> getCalendarForNextAlarm(alarm.hour,alarm.minute,6)
                else -> getCalendarForNextAlarm(alarm.hour,alarm.minute,7)
            }
        }
    }

    private fun getCalendarForNextAlarm(hour: Int,min: Int,interval: Int): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR,hour)
            set(Calendar.MINUTE,min)
            add(Calendar.DAY_OF_YEAR,interval)
        }.timeInMillis
    }



}