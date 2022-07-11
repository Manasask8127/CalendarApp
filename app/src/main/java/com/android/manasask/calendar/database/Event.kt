package com.android.manasask.calendar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "event_table")
data class Event(

    @PrimaryKey(autoGenerate = true)
    var eventId: Int = 0,

    @ColumnInfo(name = "event_title")
    var eventTitle: String,

    @ColumnInfo(name = "event_location")
    var eventLocation: String,

    @ColumnInfo(name = "start_date")
    var eventStartDate: Date,

    @ColumnInfo(name = "start_time")
    var eventStartTime: String,

    @ColumnInfo(name = "end_date")
    var eventEndDate: Date,

    @ColumnInfo(name = "end_time")
    var eventEndTime: String,

    @ColumnInfo(name = "event_description")
    var eventDesscription: String


)