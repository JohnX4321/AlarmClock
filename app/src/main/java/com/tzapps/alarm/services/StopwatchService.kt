package com.tzapps.alarm.services

import android.app.*
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.tzapps.alarm.ClockApp
import com.tzapps.alarm.MainActivity
import com.tzapps.alarm.R
import com.tzapps.alarm.utils.AppConst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopwatchService: Service() {

    private lateinit var notification: Notification
    private lateinit var runnable: Runnable
    var thread: Thread?=null
    var hours=0
    var min=0
    var sec=0
    val binder = ServiceBinder()
    var callback: StopwatchCallback? = null
    var running = false
    private lateinit var nm: NotificationManager
    private lateinit var notificationCompat: NotificationCompat.Builder

    override fun onCreate() {
        super.onCreate()
        nm = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            nm.createNotificationChannel(NotificationChannel(AppConst.CHANNEL_ID,"Stopwatch",NotificationManager.IMPORTANCE_DEFAULT).apply {
                setShowBadge(true)
                setBypassDnd(true)
            })
        }
        val i = Intent(this,MainActivity::class.java).apply {
            putExtra(AppConst.KEY_FRAG_ID,R.id.frag_stop_watch)
        }
        notificationCompat = NotificationCompat.Builder(this,AppConst.CHANNEL_ID)
            //.setUsesChronometer(true)
            .setContentTitle("Timer")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.ic_hourglass_empty_black_24dp)
            .setContentIntent(PendingIntent.getActivity(this,0, i,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE))
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .apply {
                if (isRunning) {
                    addAction(NotificationCompat.Action(android.R.drawable.ic_media_pause,"Pause",PendingIntent.getBroadcast(ClockApp.context,0,Intent(AppConst.ACTION_PAUSE_STOPWATCH),PendingIntent.FLAG_UPDATE_CURRENT)))
                } else {
                    addAction(NotificationCompat.Action(android.R.drawable.ic_media_play,"Resume",PendingIntent.getBroadcast(ClockApp.context,0,Intent(AppConst.ACTION_RESUME_STOPWATCH),PendingIntent.FLAG_UPDATE_CURRENT)))
                    addAction(NotificationCompat.Action(android.R.drawable.ic_menu_delete,"Cancel",
                        PendingIntent.getBroadcast(ClockApp.context,0,Intent(AppConst.ACTION_CANCEL_STOPWATCH),PendingIntent.FLAG_UPDATE_CURRENT)))
                }
            }
        //.setOngoing(true)
        notification = notificationCompat.build()
        Log.d("SVC","created")
        startForeground(100,notification)
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        runnable=CountDownRunner()
        thread=Thread(runnable)
        startThread()
        return START_STICKY
    }



    inner class CountDownRunner: Runnable {
        override fun run() {
            while (isRunning) {
                try{
                    startCount()
                    Thread.sleep(1000)
                } catch (e: InterruptedException){

                } catch (e: Exception) {}
            }
        }

    }
    private var pausedAtValue = 0L

    /*fun pauseNotification() {
        notification = notificationCompat.apply {
            setContentText("$hours:$min:$sec")
        }.build()
        nm.notify(100,notification)
    }*/

    fun updateNotification() {
        notification = notificationCompat.apply {
            setContentText("$hours:$min:$sec")
        }.build()
        nm.notify(100,notification)
    }

    /*fun resumeNotification() {
        notification = notificationCompat.apply {
            //setContentText("Running")
        }.build()
        nm.notify(100,notification)
    }*/

    fun resetNotification() {
        notification = notificationCompat.apply {
            setContentText("00:00:00")
        }.build()
        hours=0
        min=0
        sec=0
        nm.notify(100,notification)
    }

    private fun startCount(){
        CoroutineScope(Dispatchers.Main).launch {
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
                callback?.updateUIText(hours,min,sec)
                updateNotification()
            } catch (e: Exception) {e.printStackTrace()}
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun startThread() {
        if (thread==null){
            thread= Thread(runnable)
        }
        isRunning=true;
        thread?.start()
    }

    fun stopThread() {
        thread?.interrupt()
        isRunning=false
        thread=null
    }

    inner class ServiceBinder: Binder() {
        fun getService() = this@StopwatchService
    }

    companion object {
        var isRunning = true;
        interface StopwatchCallback {
            fun updateUIText(h: Int,m: Int,s: Int)
        }
    }

}