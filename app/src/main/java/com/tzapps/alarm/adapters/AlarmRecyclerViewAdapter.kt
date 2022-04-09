package com.tzapps.alarm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.alarm.R
import com.tzapps.alarm.listeners.OnToggleAlarmListener
import com.tzapps.alarm.models.Alarm

class AlarmRecyclerViewAdapter(private val listener: OnToggleAlarmListener): RecyclerView.Adapter<AlarmViewHolder>() {

    var alarms = ArrayList<Alarm>()

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_alarm,parent,false),listener)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(alarms[position])
    }

    fun setAlarmsList(alarms: ArrayList<Alarm>) {
        this.alarms=alarms
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return alarms.size
    }

    override fun onViewRecycled(holder: AlarmViewHolder) {
        super.onViewRecycled(holder)
        holder.alarmStarted.setOnCheckedChangeListener(null)
    }

}