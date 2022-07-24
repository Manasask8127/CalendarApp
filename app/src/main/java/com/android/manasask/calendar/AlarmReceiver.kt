package com.android.manasask.calendar

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
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
const val FLAGS = 0

class AlarmReceiver : BroadcastReceiver() {



    @SuppressLint("WrongConstant")
    override fun onReceive(context: Context?, intent: Intent?) {

        // Create an explicit intent for an Activity in your app
        val cancelIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, cancelIntent, PendingIntent.FLAG_IMMUTABLE)

        if (intent?.getIntExtra("requestCode", 0) == REQUEST_CODE) {

            val snoozeIntent = Intent(context, SnoozeReceiver::class.java)
            val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                snoozeIntent,
                PendingIntent.FLAG_IMMUTABLE)
            val notificationID = intent.getLongExtra("notificationID", 0).toInt()
            Log.d("Receiver", "request code ${intent?.getIntExtra("requestCode", 0)}")

            val notification = context?.let {
                NotificationCompat.Builder(it, channelID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(intent?.getStringExtra(titleExtra))
                    .setContentText(intent?.getStringExtra(messageExtra))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .addAction(
                        R.drawable.ic_calendar_day,
                        context.getString(R.string.snooze),
                        snoozePendingIntent
                    )
                    .build()
            }


            val manager =
                context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationID, notification)
        }
    }
}