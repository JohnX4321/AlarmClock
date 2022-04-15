package com.tzapps.alarm.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tzapps.alarm.MainActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.listeners.OnToggleAlarmListener
import com.tzapps.alarm.activities.AddAlarmActivity
import com.tzapps.alarm.adapters.AlarmRecyclerViewAdapter
import com.tzapps.alarm.databinding.FragmentAlarmBinding
import com.tzapps.alarm.models.Alarm
import com.tzapps.alarm.models.AlarmListViewModelFactory
import com.tzapps.alarm.models.AlarmsListViewModel

class AlarmListFragment: Fragment(), OnToggleAlarmListener {

    private lateinit var alarmRecyclerViewAdapter: AlarmRecyclerViewAdapter
    private lateinit var alarmListViewModel : AlarmsListViewModel
    private lateinit var binding: FragmentAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmRecyclerViewAdapter= AlarmRecyclerViewAdapter(this)
        alarmListViewModel=ViewModelProvider(this,AlarmListViewModelFactory(requireActivity().application)).get(AlarmsListViewModel::class.java)
        alarmListViewModel.alarmsLiveData.observe(this
        ) { t -> if (t != null) alarmRecyclerViewAdapter.setAlarmsList(ArrayList(t)) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmBinding.inflate(layoutInflater)
        binding.fragmentListalarmsRecylerView.layoutManager=LinearLayoutManager(context)
        binding.fragmentListalarmsRecylerView.adapter=alarmRecyclerViewAdapter

        binding.fab.setOnClickListener {
            startActivity(Intent(requireActivity(), AddAlarmActivity::class.java))
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!MainActivity.isBottomNavShown) {
            (requireActivity() as MainActivity).showNav()
        }
    }


    override fun onToggle(alarm: Alarm) {
        if (alarm.isStarted) {
            alarm.cancelAlarm(requireContext())
            alarmListViewModel.update(alarm)
        } else{
            alarm.schedule(requireContext())
            alarmListViewModel.update(alarm)
        }
    }

    companion object {
        private var f: AlarmListFragment? = null
        fun getInstance() : AlarmListFragment {
            if (f==null)
                f= AlarmListFragment()
            return f!!
        }
    }

}