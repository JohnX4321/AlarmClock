package com.tzapps.alarm.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.alarm.R
import com.tzapps.alarm.listeners.OnToggleAlarmListener
import com.tzapps.alarm.models.Alarm

class AlarmViewHolder(@NonNull val itemView: View, val listener: OnToggleAlarmListener): RecyclerView.ViewHolder(itemView) {

    val alarmTime = itemView.findViewById<TextView>(R.id.item_alarm_time)
    val alarmStarted = itemView.findViewById<SwitchCompat>(R.id.item_alarm_started)
    val alarmRecurring = itemView.findViewById<ImageView>(R.id.item_alarm_recurring)
    val alarmRecurringDays = itemView.findViewById<TextView>(R.id.item_alarm_recurringDays)
    val alarmTitle = itemView.findViewById<TextView>(R.id.item_alarm_title)

    fun bind(alarm: Alarm) {
        val alarmText = String.format("%02d:%02d",alarm.hour,alarm.minute)
        alarmTime.text = alarmText
        alarmStarted.isChecked=alarm.isStarted
        if (alarm.isRecurring) {
            alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp)
            alarmRecurringDays.text=alarm.recurringDaysText
        } else {
            alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp)
            alarmRecurringDays.text="Once"
        }
        if (alarm.title.isNotEmpty()) alarmTitle.text= String.format("%s",alarm.title)
        else alarmTitle.text= String.format("%s","Alarm")
        alarmStarted.setOnCheckedChangeListener { _, _ ->  listener.onToggle(alarm) }
    }

}