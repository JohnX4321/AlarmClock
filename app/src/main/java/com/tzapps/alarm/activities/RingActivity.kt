package com.tzapps.alarm.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.background.AlarmService
import com.tzapps.alarm.databinding.ActivityAlarmCreateBinding
import com.tzapps.alarm.databinding.ActivityWakeupBinding
import com.tzapps.alarm.models.Alarm
import com.tzapps.alarm.ui.TwoWaySlider
import java.util.*
import kotlin.random.Random

class RingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityWakeupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWakeupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //dismissBtn=findViewById(R.id.activity_ring_dismiss)
        //snoozeBtn=findViewById(R.id.activity_ring_snooze)

        /*dismissBtn.setOnClickListener {
            val i=Intent(this,AlarmService::class.java)
            stopService(i)
            finish()
        }
        snoozeBtn.setOnClickListener {
            val c=Calendar.getInstance()
            c.timeInMillis=System.currentTimeMillis()
            c.add(Calendar.MINUTE,10)
            val alarm=Alarm(Random.nextInt(Int.MAX_VALUE),c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),"Snooze",System.currentTimeMillis(),true,false,false,false,false,false,false,false,false)
            alarm.schedule(applicationContext)
            val intS=Intent(applicationContext,AlarmService::class.java)
            stopService(intS)
            finish()
        }*/
        binding.dismissSnoozeSlider.apply {
            leftImg = com.google.android.material.R.drawable.mtrl_ic_cancel
            rightImg = R.drawable.ic_snooze
        }

        binding.dismissSnoozeSlider.listener = object : TwoWaySlider.TwoWaySliderListener {
            override fun onLongPress() {

            }

            override fun onSlideLeft() {
                val i=Intent(this@RingActivity,AlarmService::class.java)
                stopService(i)
                finish()
            }

            override fun onSlideRight() {
                val c=Calendar.getInstance()
                c.timeInMillis=System.currentTimeMillis()
                c.add(Calendar.MINUTE,10)
                val alarm=Alarm(Random.nextInt(Int.MAX_VALUE),c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),"Snooze",System.currentTimeMillis(),true,false,false,false,false,false,false,false,false)
                alarm.schedule(applicationContext)
                val intS=Intent(applicationContext,AlarmService::class.java)
                stopService(intS)
                finish()
            }
        }

        val rotAnim=ObjectAnimator.ofFloat(binding.activityRingClock,"rotation",0f,20f,0f,-20f,0f)
        rotAnim.repeatCount=ValueAnimator.INFINITE
        rotAnim.duration=800
        rotAnim.start()
    }

}