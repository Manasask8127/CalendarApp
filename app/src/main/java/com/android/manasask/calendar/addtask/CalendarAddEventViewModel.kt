package com.android.manasask.calendar

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.android.manasask.calendar.addtask.REQUEST_CODE
import com.android.manasask.calendar.database.Event
import com.android.manasask.calendar.database.EventDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
//import java.sql.Date
import java.sql.SQLException
import java.util.*

class CalendarAddEventViewModel(private val database: EventDatabase, private val context: Context) :
    ViewModel() {

    private var _eventAdded = MutableLiveData(false)
    val eventAdded: LiveData<Boolean>
        get() = _eventAdded

    private var _currentTime: Long = 0
    val currentTime: Long
        get() = _currentTime

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
                    Log.d("EVENT", "id is ${id}")
                    if (id > 0)
                        _eventAdded.postValue(true)
                    registerForNotification(id)

                } catch (e: SQLException) {
                    Timber.d("DB insertion exception: ${e.message}")
                }
            }
        }
    }

    fun updateCurrentTime(time: Long) {
        _currentTime = time
    }


    private suspend fun registerForNotification(id: Long) =
        withContext(Dispatchers.IO) {
            val event = database.eventDatabaseDao.get(id)

            val intent = Intent(context, AlarmReceiver::class.java)
            val title = event.eventTitle
            val message = event.eventDesscription
            intent.putExtra(titleExtra, title)
            intent.putExtra(messageExtra, message)
            intent.putExtra("requestCode", REQUEST_CODE)
            intent.putExtra("notificationID", event.eventId)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                currentTime,
                60 * 5 * 1000,
                pendingIntent
            )

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
class CalendarAddEventViewModelFactory(
    private val database: EventDatabase,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarAddEventViewModel::class.java)) {
            return CalendarAddEventViewModel(database, context) as T
        }
        throw ClassNotFoundException("unknown view model class")
    }
}