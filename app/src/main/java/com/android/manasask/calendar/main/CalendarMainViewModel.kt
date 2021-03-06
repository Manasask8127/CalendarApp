package com.android.manasask.calendar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.manasask.calendar.database.Event
import com.android.manasask.calendar.database.getDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class CalendarMainViewModel(private val applicationContext: Application) :
    AndroidViewModel(applicationContext) {
    private var database = getDatabase(applicationContext)

    private var _eventList = MutableLiveData<List<Event>>()
    val eventList: LiveData<List<Event>>
        get() = _eventList

    private var allEvents= listOf<Event>()

    init {
        Timber.d("initializing main viewmodel")
        loadEventDatabase(Date())
        allEvents()
    }

    fun setEventDate(date: Date) {
        loadEventDatabase(date)
    }

    private fun loadEventDatabase(date: Date) {
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                database.eventDatabaseDao.getEvents(date)
                    .collect {
                       // Log.d("Manasa list", it.toString())
                        Timber.d("eventList ${it}")
                        _eventList.postValue(it)
                    }

            }
        }
    }

    private fun allEvents(){
        viewModelScope.launch {
            launch(Dispatchers.IO){
                database.eventDatabaseDao.getAllEvents().collect{
                    allEvents=it
                }
            }
        }
    }

    fun getAllEvents():List<Event>{
        return allEvents
    }

}