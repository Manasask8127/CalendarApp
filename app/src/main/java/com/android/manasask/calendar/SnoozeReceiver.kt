package com.android.manasask.calendar

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.text.format.DateUtils
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat

class SnoozeReceiver: BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        //val notiID=intent.getIntExtra("requestCode",0)
        val triggerTime = SystemClock.elapsedRealtime() + DateUtils.MINUTE_IN_MILLIS

        val notifyIntent = Intent(context, AlarmReceiver::class.java)
        val notifyPendingIntent = PendingIntent.getBroadcast(
            context,
            intent.getIntExtra("requestCode",0),
            notifyIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        AlarmManagerCompat.setExactAndAllowWhileIdle(
            alarmManager,
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            triggerTime,
            notifyPendingIntent
        )

//        val notificationManager = ContextCompat.getSystemService(
//            context,
//            NotificationManager::class.java
//        ) as NotificationManager
//        notificationManager.cancel(notiID)
    }

    }
