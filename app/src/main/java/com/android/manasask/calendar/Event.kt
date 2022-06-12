package com.android.manasask.calendar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(

    @PrimaryKey(autoGenerate = true)
    var eventId : Int=0,

    @ColumnInfo(name = "event_title")
    var eventTitle : String,

    @ColumnInfo(name = "event_location")
    var eventLocation : String,

    @ColumnInfo(name = "start_date")
    var  eventStartDate: String ,

    @ColumnInfo(name = "start_time")
    var  eventStartTime: String ,

    @ColumnInfo(name = "end_date")
    var  eventEndDate: String ,

    @ColumnInfo(name = "end_time")
    var  eventEndTime: String ,

    @ColumnInfo(name = "event_description")
    var  eventDesscription: String


)