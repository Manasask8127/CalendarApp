package com.android.manasask.calendar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
//import java.sql.Date
import java.util.*

@Dao
interface EventDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event): Long

    @Query("SELECT * FROM event_table WHERE eventId= :key")
    fun get(key: Long): Event

    @Query("SELECT * FROM EVENT_TABLE ORDER BY eventId DESC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM EVENT_TABLE WHERE :date>=start_date AND :date<=end_date ORDER BY start_date ASC")
    fun getEvents(date: Date): Flow<List<Event>>
}