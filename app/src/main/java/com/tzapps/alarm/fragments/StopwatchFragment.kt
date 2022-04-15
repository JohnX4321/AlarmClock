package com.tzapps.alarm.fragments

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tzapps.alarm.ClockApp
import com.tzapps.alarm.MainActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.databinding.FragmentStopwatchBinding
import com.tzapps.alarm.services.StopwatchService
import com.tzapps.alarm.utils.AppConst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopwatchFragment: Fragment(),StopwatchService.Companion.StopwatchCallback {
    

    companion object {
        private var f: StopwatchFragment? = null
        fun getInstance() : StopwatchFragment {
            if (f==null)
                f= StopwatchFragment()
            return f!!
        }
    }

    private lateinit var binding: FragmentStopwatchBinding
    private var boundService: StopwatchService? = null
    private val stopwatchServiceIntent = Intent(ClockApp.context,StopwatchService::class.java)

    var uiState=0
    var isRunning=false
    private val stopwatchServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val localBinder = service as StopwatchService.ServiceBinder
            boundService = localBinder.getService()
            boundService?.callback = this@StopwatchFragment
            Log.d("SVC","Binder connected")
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }



    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        updateUI(uiState)
    }

    private fun formatNumber(num: Int) = if (num<10) "0$num" else "$num"

    private fun updateUI(state: Int) {
        when(state) {
            0->{
                isRunning=false
                binding.stopWatchFrame.text="00:00:00"
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

    val StopWatchReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?: return
            if (intent.action==AppConst.ACTION_PAUSE_STOPWATCH) {
                binding.startStopButton.performClick()
            }
            else if (intent.action==AppConst.ACTION_RESUME_STOPWATCH) {
                binding.resumeButton.performClick()
            }
            else if (intent.action==AppConst.ACTION_CANCEL_STOPWATCH) {
                binding.resetButton.performClick()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().registerReceiver(StopWatchReceiver,IntentFilter()
            .apply
         {
             addAction(AppConst.ACTION_PAUSE_STOPWATCH)
             addAction(AppConst.ACTION_RESUME_STOPWATCH)
             addAction(AppConst.ACTION_CANCEL_STOPWATCH)
         })
    }

    override fun onDestroy() {
        requireActivity().unregisterReceiver(StopWatchReceiver)
        super.onDestroy()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStopwatchBinding.inflate(inflater)
        (requireActivity() as MainActivity).showNav()
        binding.startStopButton.setOnClickListener {
            if (!isRunning) {
                ContextCompat.startForegroundService(ClockApp.context,stopwatchServiceIntent)
                requireActivity().bindService(stopwatchServiceIntent,stopwatchServiceConnection,Context.BIND_AUTO_CREATE)
                updateUI(1)
            } else {
                boundService?.stopThread()
                updateUI(2)
                boundService?.updateNotification()
            }
        }
        binding.resumeButton.setOnClickListener {
            boundService?.startThread();
            updateUI(1)
            boundService?.updateNotification()
        }
        binding.resetButton.setOnClickListener {
            boundService?.stopThread()
            boundService?.resetNotification()
            updateUI(0)
            requireActivity().unbindService(stopwatchServiceConnection)
            requireActivity().stopService(stopwatchServiceIntent)
        }

        return binding.root
    }

    override fun updateUIText(h: Int, m: Int, s: Int) {
        setStopwatchText(h, m, s)
    }




}