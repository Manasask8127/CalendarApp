package com.android.manasask.calendar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class CalendarMainViewModel(private val applicationContext: Application) :
    AndroidViewModel(applicationContext) {
    private var database = getDatabase(applicationContext)

    private var _eventList = MutableLiveData<List<Event>>()
    val eventList: LiveData<List<Event>>
        get() = _eventList

    init {
        Timber.d("initializing main viewmodel")
        loadEventDatabase(Date())
    }

    fun setEventDate(date: Date) {
        loadEventDatabase(date)
    }

    private fun loadEventDatabase(date: Date) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                database.eventDatabaseDao.getEvents(date)
                    .collect {
                        Log.d("Manasa list", it.toString())
                        Timber.d("eventList ${it}")
                        _eventList.postValue(it)
                    }

            }
        }
    }

}