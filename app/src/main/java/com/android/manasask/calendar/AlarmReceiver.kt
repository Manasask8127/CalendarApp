package com.android.manasask.calendar

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.manasask.calendar.addtask.REQUEST_CODE
import java.lang.Math.random
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt


const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.getIntExtra("requestCode", 0) == REQUEST_CODE) {
            val notificationID = intent.getLongExtra("notificationID", 0).toInt()
            Log.d("Receiver", "request code ${intent?.getIntExtra("requestCode", 0)}")

            val notification = context?.let {
                NotificationCompat.Builder(it, channelID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(intent?.getStringExtra(titleExtra))
                    .setContentText(intent?.getStringExtra(messageExtra))
                    .build()
            }


            val manager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationID, notification)
        }
    }
}