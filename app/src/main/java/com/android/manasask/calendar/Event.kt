package com.android.manasask.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(

    @PrimaryKey(autoGenerate = true)
    var eventId : Long = 0L,

    @ColumnInfo(name = "event_title")
    var eventTitle : String,

    @ColumnInfo(name = "event_location")
    var eventLocation : String,

    @ColumnInfo(name = "start_date")
    var  eventStartDate: Int ,

    @ColumnInfo(name = "start_time")
    var  eventStartTime: Int ,

    @ColumnInfo(name = "end_date")
    var  eventEndDate: Int ,

    @ColumnInfo(name = "end_time")
    var  eventEndTime: Int ,

    @ColumnInfo(name = "event_description")
    var  eventDesscription: String


)