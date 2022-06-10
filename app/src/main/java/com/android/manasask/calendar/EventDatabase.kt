package com.android.manasask.calendar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Event::class],version = 1,exportSchema = false)
abstract class EventDatabase: RoomDatabase(){
    abstract val eventDatabaseDao : EventDatabaseDao

    companion object
    {
        @Volatile
        private var INSTANCE : EventDatabase?= null

        fun getInstance(context : Context) : EventDatabase
        {
            synchronized(this)
            {
                var instance= INSTANCE

                if(instance==null)
                {
                    instance= Room.databaseBuilder(
                        context.applicationContext  ,
                        EventDatabase::class.java,
                        "event_history_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }

}