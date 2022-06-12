package com.android.manasask.calendar

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: Event):Long

    @Query("SELECT * FROM event_table WHERE eventId= :key")
    fun get(key: Long):Event

    @Query("SELECT * FROM EVENT_TABLE ORDER BY eventId DESC")
    fun getAllEvents(): LiveData<List<Event>>

    @Query("SELECT * FROM EVENT_TABLE WHERE start_date=:startDate ORDER BY start_date ASC")
    fun getEvents(startDate: String): Flow<List<Event>>
}