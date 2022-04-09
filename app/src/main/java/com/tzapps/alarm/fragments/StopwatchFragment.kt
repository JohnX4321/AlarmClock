package com.tzapps.alarm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.tzapps.alarm.MainActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.databinding.FragmentStopwatchBinding

class StopwatchFragment: Fragment() {
    
    private lateinit var runnable: Runnable
    private lateinit var binding: FragmentStopwatchBinding
    var thread: Thread?=null
    var uiState=0
    var hours=0
    var min=0
    var sec=0
    var isRunning=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runnable=CountDownRunner()
        thread=Thread(runnable)
    }

    inner class CountDownRunner: Runnable {
        override fun run() {
            while (!Thread.currentThread().isInterrupted) {
                try{
                    startCount()
                    Thread.sleep(1000)
                } catch (e: InterruptedException){
                    Thread.currentThread().interrupt()
                } catch (e: Exception) {}
            }
        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        updateUI(uiState)
        setStopwatchText(hours,min,sec)
    }

    private fun formatNumber(num: Int) = if (num<10) "0$num" else "$num"

    private fun updateUI(state: Int) {
        when(state) {
            0->{
                isRunning=false
                binding.stopWatchFrame.text="00:00:00"
                sec=0
                hours=0
                min=0
                binding.startStopButton.text=getString(R.string.start)
                binding.startStopButton.visibility=View.VISIBLE
                binding.resumeButton.visibility=View.INVISIBLE
                binding.resetButton.visibility=View.INVISIBLE
                uiState=0
            }
            1->{
                isRunning=true
                binding.startStopButton.text=getString(R.string.stop)
                binding.startStopButton.visibility=View.VISIBLE
                binding.resumeButton.visibility=View.INVISIBLE
                binding.resetButton.visibility=View.INVISIBLE
                uiState=1
            }
            2->{
                binding.startStopButton.visibility=View.INVISIBLE
                binding.resumeButton.visibility=View.VISIBLE
                binding.resetButton.visibility=View.VISIBLE
            }
        }
    }

    private fun setStopwatchText(h: Int,m: Int,s: Int){
        binding.stopWatchFrame.text=formatNumber(h)+":"+formatNumber(m)+":"+formatNumber(s)
    }

    private fun startCount(){
        requireActivity().runOnUiThread {
            try{
                sec++
                if (sec==60) {
                    sec=0
                    min++
                }
                if (min==60) {
                    min=0
                    hours++
                }
                setStopwatchText(hours,min,sec)
            } catch (e: Exception) {e.printStackTrace()}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentStopwatchBinding.inflate(inflater)
        (requireActivity() as MainActivity).showNav()
        binding.startStopButton.setOnClickListener {
            if (!isRunning) {
                thread?.start()
                updateUI(1)
            } else {
                thread?.interrupt()
                updateUI(2)
            }
        }
        binding.resumeButton.setOnClickListener {
            runnable=CountDownRunner()
            thread=Thread(runnable)
            thread?.start()
            updateUI(1)
        }
        binding.resetButton.setOnClickListener {
            thread?.interrupt()
            runnable=CountDownRunner()
            thread= Thread(runnable)
            updateUI(0)
        }

        return binding.root
    }


}