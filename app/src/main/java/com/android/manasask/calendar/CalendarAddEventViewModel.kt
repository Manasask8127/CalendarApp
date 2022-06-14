package com.android.manasask.calendar

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
//import java.sql.Date
import java.sql.SQLException
import java.util.*

class CalendarAddEventViewModel(private val database: EventDatabase) : ViewModel() {

    private var _eventAdded = MutableLiveData(false)
    val eventAdded: LiveData<Boolean>
        get() = _eventAdded

    /**
     * insert the [Event] which created based on user selection
     * @exception SQLException logs any insertion to database fails
     */
    fun insertItem(
        tile: String, location: String, startdate: Date, startTime: String,
        endDate: Date, endTime: String, description: String
    ) {
        val databaseEventItem = Event(
            eventTitle = tile,
            eventLocation = location,
            eventStartDate = startdate,
            eventStartTime = startTime,
            eventEndDate = endDate,
            eventEndTime = endTime,
            eventDesscription = description,
        )
        var id: Long
        viewModelScope.launch {
            launch(Dispatchers.IO) {
                try {
                    id = database.eventDatabaseDao.insert(databaseEventItem)
                    if (id > 0)
                        _eventAdded.postValue(true)

                } catch (e: SQLException) {
                    Timber.d("DB insertion exception: ${e.message}")
                }
            }
        }
    }

    fun clearItemAdded(value: Boolean) {
        _eventAdded.value = value
    }
}


/**
 * A Factory class to create ViewModel [CalendarAddEventViewModel]
 * @param[database] takes [EventDatabase]
 * @throws ClassNotFoundException
 */
class CalendarAddEventViewModelFactory(private val database: EventDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarAddEventViewModel::class.java)) {
            return CalendarAddEventViewModel(database) as T
        }
        throw ClassNotFoundException("unknown view model class")
    }
}