package com.tzapps.alarm.background

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.tzapps.alarm.ClockApp
import com.tzapps.alarm.R
import com.tzapps.alarm.activities.RingActivity
import com.tzapps.alarm.utils.AppConst

class AlarmService: Service() {

    lateinit var vibrator: Vibrator
    lateinit var ringtone: Ringtone

    override fun onCreate() {
        super.onCreate()
        vibrator = getSystemService(Vibrator::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notIntent = Intent(this, RingActivity::class.java)
        val pi=PendingIntent.getActivity(this,0,notIntent,0)
        val title=String.format("%s Alarm",intent!!.getStringExtra(AppConst.TITLE))
        val notif = NotificationCompat.Builder(this, AppConst.CHANNEL_ID)
            .setContentText(title).setContentText("ALARM!!!")
            .setSmallIcon(R.drawable.ic_alarm_black_24dp).setFullScreenIntent(pi,true).build()
        ringtone = RingtoneManager.getRingtone(applicationContext,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        ringtone.play()
        vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0,100,1000),0))
        startForeground(1,notif)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator.cancel()
        ringtone.stop()
    }

    @Nullable
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}