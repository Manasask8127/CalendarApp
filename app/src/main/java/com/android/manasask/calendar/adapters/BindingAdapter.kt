package com.android.manasask.calendar

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.manasask.calendar.database.Event
import java.text.SimpleDateFormat
import java.util.*

/**
 * Binding Adapter for TextView in each_item.xml
 * @param[textView] widget on which start time load
 * @param[event] data model of type [Event]
 */
@SuppressLint("SimpleDateFormat")
@BindingAdapter("startText")
fun bindStartText(textView: TextView, event: Event) {

    textView.text =
        (SimpleDateFormat("MM/dd/yyyy").format(event.eventStartDate)) +" "+ event.eventStartTime
}


@SuppressLint("SimpleDateFormat")
@BindingAdapter("endText")
fun bindEndText(textView: TextView, event: Event) {
    val date = Date(event.eventEndDate.time - (24 * 60 * 60 * 1000))
    // date.
    textView.text = (SimpleDateFormat("MM/dd/yyyy").format(date)) +" "+ event.eventEndTime
}