package com.tzapps.alarm.models

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tzapps.alarm.background.AlarmReceiver
import com.tzapps.alarm.utils.AppConst
import com.tzapps.alarm.utils.DayUtils
import com.tzapps.alarm.utils.Utils
import java.util.*


@Entity(tableName = "alarm_table")
class Alarm(
    @PrimaryKey var alarmId: Int,
    val hour: Int,
    val minute: Int,
    val title: String,
    var created: Long,
    var isStarted: Boolean,
    val isRecurring: Boolean,
    val isMonday: Boolean,
    val isTuesday: Boolean,
    val isWednesday: Boolean,
    val isThursday: Boolean,
    val isFriday: Boolean,
    val isSaturday: Boolean,
    val isSunday: Boolean
) {

    //TODO use Exact alarm and reschedule functionality

    @SuppressLint("DefaultLocale")
    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(AppConst.KEY_ALARM_ID,alarmId)
        intent.putExtra(AppConst.RECURRING, isRecurring)
        intent.putExtra(AppConst.MONDAY, isMonday)
        intent.putExtra(AppConst.TUESDAY, isTuesday)
        intent.putExtra(AppConst.WEDNESDAY, isWednesday)
        intent.putExtra(AppConst.THURSDAY, isThursday)
        intent.putExtra(AppConst.FRIDAY, isFriday)
        intent.putExtra(AppConst.SATURDAY, isSaturday)
        intent.putExtra(AppConst.SUNDAY, isSunday)
        intent.putExtra(AppConst.TITLE, title)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId, intent, if (Utils.isAboveS()) (PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE) else PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }
        if (!isRecurring) {
            var toastText: String? = null
            try {
                toastText = java.lang.String.format(
                    "One Time Alarm %s scheduled for %s at %02d:%02d",
                    title, DayUtils.toDay(calendar.get(Calendar.DAY_OF_WEEK)),
                    hour,
                    minute,
                    alarmId
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmPendingIntent
            )
        } else {
            val toastText = String.format(
                "Recurring Alarm %s scheduled for %s at %02d:%02d",
                title,
                recurringDaysText,
                hour,
                minute,
                alarmId
            )
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                RUN_DAILY,
                alarmPendingIntent
            )
        }
        isStarted = true
    }

    fun cancelAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId, intent, 0
        )
        alarmManager.cancel(alarmPendingIntent)
        isStarted = false
        val toastText = String.format(
            "Alarm cancelled for %02d:%02d with id %d",
            hour,
            minute,
            alarmId
        )
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
        Log.i("cancel", toastText)
    }

    val recurringDaysText: String?
        get() {
            if (!isRecurring) {
                return null
            }
            var days = ""
            if (isMonday) {
                days += "Mo "
            }
            if (isTuesday) {
                days += "Tu "
            }
            if (isWednesday) {
                days += "We "
            }
            if (isThursday) {
                days += "Th "
            }
            if (isFriday) {
                days += "Fr "
            }
            if (isSaturday) {
                days += "Sa "
            }
            if (isSunday) {
                days += "Su "
            }
            return days
        }

}