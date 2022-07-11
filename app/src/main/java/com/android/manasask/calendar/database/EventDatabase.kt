package com.android.manasask.calendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.manasask.calendar.Converters

@Database(entities = [Event::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract val eventDatabaseDao: EventDatabaseDao
}

@Volatile
private lateinit var database: EventDatabase

fun getDatabase(context: Context): EventDatabase {
    synchronized(EventDatabase::class.java)
    {
        if (!::database.isInitialized) {
            database = Room.databaseBuilder(
                context,
                EventDatabase::class.java, "event_table"
            )
                .fallbackToDestructiveMigration()
                .build()

        }
        return database
    }
}


