package com.tzapps.alarm.fragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tzapps.alarm.MainActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.databinding.FragmentTimerBinding
import java.util.concurrent.TimeUnit

class TimerFragment(): Fragment() {

    
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var binding: FragmentTimerBinding

    var secFromNP=0
    var minFromNP=0
    var hourFromNP=0
    var uiState=0
    var sec=0L
    var min=0L
    var hours=0L
    var totalMs=0L
    var isRunning=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTimerBinding.inflate(layoutInflater)
        (requireActivity() as MainActivity).showNav()
        binding.startStopButton.setOnClickListener {
            if (!isRunning) {
                updateUI(1)
                totalMs=(hourFromNP*3600000+minFromNP*60000+(secFromNP+1)*1000).toLong()
                startTimer(totalMs)
            } else{
                updateUI(2)
                countDownTimer.cancel()
            }

        }

        
        binding.resumeButton.setOnClickListener {
            updateUI(1)
            setTimerText(hours,min,sec)
            totalMs=(hours*3600000+min*60000+sec*1000)
            startTimer(totalMs)
        }
        
        binding.resetButton.setOnClickListener {
            countDownTimer.cancel()
            updateUI(0)
        }
        
        binding.secondsPicker.apply {
            minValue=0
            maxValue=60
        }
        binding.secondsPicker.setOnValueChangedListener { numberPicker, i, i2 -> secFromNP=numberPicker.value }
        
        binding.minutesPicker.apply {
            minValue=0
            maxValue=60
        }
        binding.minutesPicker.setOnValueChangedListener { numberPicker, i, i2 -> minFromNP=numberPicker.value  }
        
        binding.hoursPicker.apply {
            minValue=0
            maxValue=99
        }
        binding.hoursPicker.setOnValueChangedListener { numberPicker, i, i2 -> hourFromNP=numberPicker.value }

        return binding.root
    }

    private fun startTimer(time: Long) {
        countDownTimer=object : CountDownTimer(time,1000) {
            override fun onTick(p0: Long) {
                hours=TimeUnit.MILLISECONDS.toHours(p0)
                min=TimeUnit.MILLISECONDS.toMinutes(p0)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(p0))
                sec=TimeUnit.MILLISECONDS.toSeconds(p0)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(p0))
                setTimerText(hours,min,sec)
            }

            override fun onFinish() {
                updateUI(0)
            }

        }.start()
    }

    private fun formatNumber(num: Long) = if (num<10) "0$num" else "$num"

    private fun setTimerText(h: Long,m: Long,s: Long) {binding.timerFrame.text= formatNumber(h)+""+formatNumber(min)+":"+formatNumber(s)}

    private fun updateUI(state: Int){
        when(state) {
            0->{
                isRunning=false
                binding.startStopButton.text="Start"
                binding.picker.visibility=View.VISIBLE
                binding.timerFrame.visibility=View.INVISIBLE
                binding.startStopButton.visibility=View.VISIBLE
                binding.resetButton.visibility=View.INVISIBLE
                binding.resumeButton.visibility=View.INVISIBLE
                uiState=0
            }
            1->{
                isRunning=true
                binding.startStopButton.text="Stop"
                binding.picker.visibility=View.INVISIBLE
                binding.timerFrame.visibility=View.VISIBLE
                binding.startStopButton.visibility=View.VISIBLE
                binding.resumeButton.visibility=View.INVISIBLE
                binding.resetButton.visibility=View.INVISIBLE
                uiState=1
            }
            2->{
                binding.picker.visibility=View.INVISIBLE
                binding.timerFrame.visibility=View.VISIBLE
                binding.startStopButton.visibility=View.INVISIBLE
                binding.resumeButton.visibility=View.VISIBLE
                binding.resetButton.visibility=View.VISIBLE
                uiState=2
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI(uiState)
        setTimerText(hours,min,sec)
    }

}