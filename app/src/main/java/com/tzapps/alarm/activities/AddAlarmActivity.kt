package com.tzapps.alarm.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tzapps.alarm.R
import com.tzapps.alarm.databinding.ActivityAlarmCreateBinding
import com.tzapps.alarm.models.Alarm
import com.tzapps.alarm.models.AlarmViewModel
import com.tzapps.alarm.models.AlarmViewModelFactory
import com.tzapps.alarm.utils.TimePickerUtils
import kotlin.random.Random

class AddAlarmActivity : AppCompatActivity() {

    private lateinit var timePicker: TimePicker
    private lateinit var title: EditText
    private lateinit var scheduleAlarm: Button
    private lateinit var recurring: CheckBox
    private lateinit var mon: CheckBox
    private lateinit var tues: CheckBox
    private lateinit var wed: CheckBox
    private lateinit var thur: CheckBox
    private lateinit var fri: CheckBox
    private lateinit var sat: CheckBox
    private lateinit var sun: CheckBox
    private lateinit var recurringOptions: LinearLayout
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var binding: ActivityAlarmCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alarmViewModel = ViewModelProvider(this,AlarmViewModelFactory(application)).get(AlarmViewModel::class.java)
        timePicker = binding.fragmentCreatealarmTimePicker
        title = binding.fragmentCreatealarmTitle
        scheduleAlarm = binding.fragmentCreatealarmScheduleAlarm
        recurring = binding.fragmentCreatealarmRecurring
        mon = binding.fragmentCreatealarmCheckMon
        tues = binding.fragmentCreatealarmCheckTue
        wed = binding.fragmentCreatealarmCheckWed
        thur = binding.fragmentCreatealarmCheckThu
        fri = binding.fragmentCreatealarmCheckFri
        sat = binding.fragmentCreatealarmCheckSat
        sun = binding.fragmentCreatealarmCheckSun
        recurringOptions = binding.fragmentCreatealarmRecurringOptions
        recurring.setOnCheckedChangeListener { _, b ->
            if (b) recurringOptions.visibility=View.VISIBLE
            else recurringOptions.visibility=View.GONE
        }
        scheduleAlarm.setOnClickListener {
            scheduleAlarm()
            finish()
        }

    }

    private fun scheduleAlarm() {
        val id= Random.nextInt(Int.MAX_VALUE)
        val alarm = Alarm(id,TimePickerUtils.getTimePickerHour(timePicker),TimePickerUtils.getTimePickerMinute(timePicker),title.text.toString(),System.currentTimeMillis(),true,recurring.isChecked,mon.isChecked,tues.isChecked,wed.isChecked,thur.isChecked,fri.isChecked,sat.isChecked,sun.isChecked)
        alarmViewModel.insert(alarm)
        alarm.schedule(this)
    }

}