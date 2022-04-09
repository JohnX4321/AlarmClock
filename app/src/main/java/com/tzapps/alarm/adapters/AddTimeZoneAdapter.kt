package com.tzapps.alarm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.alarm.R
import com.tzapps.alarm.models.ZoneClock
import com.tzapps.alarm.models.ZoneViewModel
import com.tzapps.alarm.models.ZoneViewModelFactory
import com.tzapps.alarm.utils.AppConst
import org.joda.time.DateTimeZone
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class AddTimeZoneAdapter(private val activity: AppCompatActivity): RecyclerView.Adapter<AddTimeZoneAdapter.AddZoneClockViewHolder>() {

    //private val timeZoneSet = DateTimeZone.getAvailableIDs()
    private lateinit var zoneClockModel: ZoneViewModel
    private var timeZoneList: ArrayList<Array<String>> = ArrayList()

    init {
        formatList()
    }

    fun formatList() {
        for (z in AppConst.PRIMARY_TIMEZONES) {
            val t=ZoneId.of(z)
            val ct = DateTimeFormatter.ofPattern("ZZZZ").format(ZonedDateTime.now(t))
            if (ct.length<5)
                continue
            timeZoneList.add(arrayOf(z,ct))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddZoneClockViewHolder {
        zoneClockModel = ViewModelProvider(activity, ZoneViewModelFactory(activity.application)).get(ZoneViewModel::class.java)
        return AddZoneClockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_clock,parent,false))
    }

    override fun onBindViewHolder(holder: AddZoneClockViewHolder, position: Int) {
        holder.onBind(timeZoneList[position])
    }

    override fun getItemCount(): Int {
        return timeZoneList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class AddZoneClockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val itemClock = itemView.findViewById<TextView>(R.id.zoneTime)
        private val itemName = itemView.findViewById<TextView>(R.id.zoneName)

        fun onBind(timeZone: Array<String>) {
            itemName.text = timeZone[0]
            itemClock.text = timeZone[1]
            itemView.setOnClickListener {
                zoneClockModel.insert(ZoneClock(timeZone[1],timeZone[0]))
                activity.finish()
            }
        }

    }

}