package com.android.manasask.calendar

import android.app.Application
import timber.log.Timber


class CalendarApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        //createNotificationChannel()
    }

}