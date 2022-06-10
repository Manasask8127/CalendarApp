package com.android.manasask.calendar

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface EventDatabaseDao {
    @Insert
    fun insert(event: Event)

    @Query("SELECT * FROM event_table WHERE eventId= :key")
    fun get(key: Long):Event

    @Query("SELECT * FROM EVENT_TABLE ORDER BY eventId DESC")
    fun getAllNights(): LiveData<List<Event>>

    @Query("SELECT * FROM EVENT_TABLE WHERE start_date=:startDate")
    fun getToNight(startDate: Int): Event?
}