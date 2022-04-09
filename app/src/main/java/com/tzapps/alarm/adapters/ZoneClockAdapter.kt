package com.tzapps.alarm.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.alarm.R
import com.tzapps.alarm.fragments.ClockFragment
import com.tzapps.alarm.models.ZoneClock
import com.tzapps.alarm.utils.Prefs

class ZoneClockAdapter(private val fragment: Fragment): RecyclerView.Adapter<ZoneClockAdapter.ZoneClockViewHolder>() {

    private val is24hours = Prefs.get24hours()
    var savedZoneList: MutableList<ZoneClock> = ArrayList()
    val pendingItemsRemoval = ArrayList<ZoneClock>()

    fun setZoneList(zoneList: MutableList<ZoneClock>) {
        this.savedZoneList = zoneList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return savedZoneList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZoneClockViewHolder {
        return ZoneClockViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_clock_main,parent,false))
    }

    override fun onBindViewHolder(holder: ZoneClockViewHolder, position: Int) {
        val t = savedZoneList[position]
        /*if (pendingItemsRemoval.contains(t)) {
            holder.itemView.setBackgroundColor(Color.RED)
            holder.itemClock.visibility = GONE
        } else*/
            holder.onBind(t,position)
    }


    inner class ZoneClockViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemClock = itemView.findViewById<TextClock>(R.id.zoneTime)
        val itemName = itemView.findViewById<TextView>(R.id.zoneName)

        fun onBind(zoneClock: ZoneClock,position: Int) {
            if (is24hours) {
                itemClock.format12Hour = null
            }
            else {
                itemClock.format24Hour = null
            }
            itemClock.timeZone = zoneClock.id
            itemName.text = zoneClock.name
            itemView.rootView.setOnLongClickListener {
                val popupMenu = PopupMenu(fragment.requireActivity(),it)
                popupMenu.menuInflater.inflate(R.menu.delete_popup,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener {
                    if (it.itemId==R.id.deleteZone) {
                        (fragment as ClockFragment).slowRemoval(position)
                        return@setOnMenuItemClickListener true
                    }
                    return@setOnMenuItemClickListener false
                }
                popupMenu.show()
                return@setOnLongClickListener true
            }
        }

    }


}