package com.tzapps.alarm.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tzapps.alarm.ClockApp
import com.tzapps.alarm.MainActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.activities.AddTimeZoneActivity
import com.tzapps.alarm.adapters.ZoneClockAdapter
import com.tzapps.alarm.databinding.FragmentClockBinding
import com.tzapps.alarm.models.AlarmViewModel
import com.tzapps.alarm.models.ZoneClock
import com.tzapps.alarm.models.ZoneViewModel
import com.tzapps.alarm.models.ZoneViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ClockFragment: Fragment() {

    private var currDay=""
    private var currDate=""
    private var currTime=""
    private var hours=0
    private var min=0
    private var sec=0
    private var days= arrayOf("")
    private lateinit var binding: FragmentClockBinding
    private lateinit var zoneAdapter: ZoneClockAdapter
    private lateinit var zoneClockViewModel: ZoneViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        days=requireActivity().resources.getStringArray(R.array.days_of_week)
        zoneAdapter = ZoneClockAdapter(this)
        zoneClockViewModel = ViewModelProvider(this, ZoneViewModelFactory(requireActivity().application)).get(ZoneViewModel::class.java)
        zoneClockViewModel.zonesLiveData.observe(this) {
            t-> if (t!=null) zoneAdapter.setZoneList(t)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentClockBinding.inflate(layoutInflater)
        binding.DaysFrame.text=getDay()
        binding.DateFrame.text=getDate()
        binding.userChoiceClocks.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.userChoiceClocks.adapter = zoneAdapter
        binding.userChoiceClocks.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.HORIZONTAL)
        )
        binding.fab.setOnClickListener {
            startActivity(Intent(requireContext(),AddTimeZoneActivity::class.java))
        }
        return binding.root
    }

    fun getCurrentTime(): String {
        val c = Calendar.getInstance()
        hours = c.get(Calendar.HOUR_OF_DAY)
        min = c.get(Calendar.MINUTE)
        sec = c.get(Calendar.SECOND)
        return getClockText(hours,min,sec)
    }

    fun getDate(): String {
        val c=Calendar.getInstance()
        currDate="${c.get(Calendar.DATE)}.${c.get(Calendar.MONTH)+1}.${c.get(Calendar.YEAR)}"
        return currDate
    }

    fun getDay(): String {
        currDay=days[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1]
        return currDay
    }

    private fun formatNumber(num: Int) = if (num<10) "0$num" else "$num"

    fun getClockText(h: Int,m: Int,s: Int) = formatNumber(h)+":"+formatNumber(m)+":"+formatNumber(s)

    override fun onResume() {
        super.onResume()
        binding.DateFrame.text=currDate
        binding.DaysFrame.text=currDay
    }



    fun slowRemoval(pos: Int) {
        val item = zoneAdapter.savedZoneList[pos]
        zoneAdapter.savedZoneList.removeAt(pos)
        zoneAdapter.notifyItemRemoved(pos)
        zoneClockViewModel.delete(item)
    }

}