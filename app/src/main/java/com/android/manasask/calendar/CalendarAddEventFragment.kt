package com.android.manasask.calendar

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.room.OnConflictStrategy.REPLACE
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.android.manasask.calendar.NotifyWork.Companion.NOTIFICATION_ID
import com.android.manasask.calendar.NotifyWork.Companion.NOTIFICATION_WORK
import com.android.manasask.calendar.databinding.FragmentCalendarAddEventBinding
import com.android.manasask.calendar.databinding.FragmentCalendarMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import timber.log.Timber
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.getDefault
import kotlin.time.DurationUnit
import java.util.concurrent.TimeUnit.MILLISECONDS

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarAddEventFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarAddEventFragment : Fragment() {

    private lateinit var viewModel: CalendarAddEventViewModel
    private lateinit var binding: FragmentCalendarAddEventBinding
    var calendar = Calendar.getInstance()
    val pickerDate = Calendar.getInstance()

    private lateinit var sDate: Date
    private lateinit var eDate: Date

    private lateinit var notificationTime:Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_calendar_add_event, container, false)
        binding = DataBindingUtil.inflate<FragmentCalendarAddEventBinding>(
            inflater,
            R.layout.fragment_calendar_add_event,
            container,
            false
        )


        // Instantiating viewmodel using factory class
        val calendarAddItemViewModelProvider =
            CalendarAddEventViewModelFactory(getDatabase(requireActivity().application))
        viewModel = ViewModelProvider(
            this@CalendarAddEventFragment,
            calendarAddItemViewModelProvider
        )[CalendarAddEventViewModel::class.java]

        // Creating toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        // binding.topAppBar.title = getString(R.string.title_add_event)
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.topAppBar.setupWithNavController(findNavController(), appBarConfiguration)

        createNotificationChannel()

        //Start date
        val startDateListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                calendar.set(p1, p2, p3)
                binding.startDateEdit.text =
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time)
                binding.endDateEdit.text =
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time)
                sDate = calendar.time
                eDate=calendar.time
            }
        }

        binding.startDateEdit.setOnClickListener {
            DatePickerDialog(
                requireContext(), startDateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //End Date
        val endDateListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                calendar.set(p1, p2, p3)

                //binding.startDateEdit.text=SimpleDateFormat("MM/dd/yyyy",Locale.US).format(calendar.time)
                //Toast.makeText(requireContext(),"calendar time ${calendar.time}",Toast.LENGTH_SHORT).show()
                Log.d("Manasa", "endDate: ${calendar.time.time}")
                binding.endDateEdit.text =
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time)
                calendar.add(Calendar.DATE, 1)
                eDate = calendar.time
                // Log.d("Manasa")
                //1655528975439
            }
        }

        binding.endDateEdit.setOnClickListener {
            DatePickerDialog(
                requireContext(), endDateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //Start Time
        val startTimeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                calendar.set(sDate.year+1900, sDate.month,sDate.date , p1, p2)
                binding.startTimeEdit.text =
                    SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)
                Log.d("Manasa"," notification time ${sDate} and ${calendar.time} and  ${sDate.year} ${sDate.month} ${sDate.date}")
                notificationTime=calendar.time
                calendar.set(0, 0, 0, p1 + 1, p2)
                binding.endTimeEdit.text =
                    SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)

            }
        }

        binding.startTimeEdit.setOnClickListener {
            TimePickerDialog(
                requireContext(), startTimeListener, calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE), true
            ).show()
        }


        //End time
        val endTimeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                calendar.set(0, 0, 0, p1, p2)
                binding.endTimeEdit.text =
                    SimpleDateFormat("HH:mm", Locale.US).format(calendar.time)
            }
        }

        binding.endTimeEdit.setOnClickListener {
            TimePickerDialog(
                requireContext(), endTimeListener, calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE), true
            ).show()
        }

        //save
        binding.save.setOnClickListener {
            closeKeyboard()
            if (checkTitleNotEmpty() && checkStartTimeNotEmpty() && checkStartDateNotEmpty()) {
                val title = binding.titleEdit.text.toString().trim()
                val location = binding.placeEdit.text.toString().trim()
                val startDate = sDate
                //java.sql.Date.valueOf(binding.startDateEdit.text.toString().trim())
                val startTime = binding.startTimeEdit.text.toString().trim()
                val endDate = eDate
                //java.sql.Date.valueOf(binding.endDateEdit.text.toString().trim())
                val endTime = binding.endTimeEdit.text.toString().trim()
                val description = binding.detailEdit.text.toString().trim()
                Timber.d(
                    "title: $title, location:$location, startDate:$startDate,startTime:$startTime" +
                            " endDate:$endDate, endTime:$endTime,description:$description"
                )
                viewModel.insertItem(
                    title,
                    location,
                    startDate,
                    startTime,
                    endDate,
                    endTime,
                    description
                )
                clearValues()
                getDateScheduleNotification()


            }

        }

        viewModel.eventAdded.observe(viewLifecycleOwner) { itemAdded ->
            if (itemAdded) {
                Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_LONG).show()

                viewModel.clearItemAdded(false)
            }

        }
        this.view?.setOnKeyListener(onBackClick())


        return binding.root
    }

    private fun createNotificationChannel() {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun getDateScheduleNotification() {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val title = binding.titleEdit.text.toString()
        val message = binding.detailEdit.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            time,
            60*5*1000
            ,
            pendingIntent
        )

    }

    private fun getTime(): Long {
        return notificationTime.time

    }

    private fun clearValues() {
        findNavController().navigate(R.id.action_calendarAddEventFragment_to_calendarMainFragment)

    }

    // closes the keyboard after clicking on save
    private fun closeKeyboard() {
        val focusedView = requireActivity().currentFocus
        val inputManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }

    // checks if entered title is not empty before saving
    private fun checkTitleNotEmpty(): Boolean {
        binding.titleEdit.text.let {
            return if (binding.titleEdit.text?.isEmpty() == true) {
                binding.titleEdit.error = getString(R.string.error_empty_title)
                false
            } else
                true

        }
    }

    //checks if starttime is not empty
    private fun checkStartTimeNotEmpty(): Boolean {
        binding.startTimeEdit.text.let {
            return if (binding.startTimeEdit.text?.isEmpty() == true) {
                binding.startTimeEdit.error = getString(R.string.error_empty_startTime)
                false
            } else
                true
        }
    }

    //checks if startdate is not empty
    private fun checkStartDateNotEmpty(): Boolean {
        binding.startDateEdit.text.let {
            return if (binding.startDateEdit.text?.isEmpty() == true) {
                binding.startDateEdit.error = getString(R.string.error_empty_startDate)
                false
            } else
                true
        }
    }

    // navigates to MainFragment on back click press
    private fun onBackClick(): View.OnKeyListener {
        return object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    findNavController().navigate(R.id.action_calendarAddEventFragment_to_calendarMainFragment)
                    return true;
                }
                return false;
            }

        }
    }




}