package com.android.manasask.calendar.addtask

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
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
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.manasask.calendar.*
import com.android.manasask.calendar.R.*
import com.android.manasask.calendar.database.getDatabase
import com.android.manasask.calendar.databinding.FragmentCalendarAddEventBinding
import com.android.manasask.calendar.receiver.channelID
import com.google.android.material.snackbar.Snackbar.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

const val REQUEST_CODE=400

class CalendarAddEventFragment : Fragment() {

    private lateinit var viewModel: CalendarAddEventViewModel
    private lateinit var binding: FragmentCalendarAddEventBinding
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    var stCalendar = Calendar.getInstance()
    var etCalendar = Calendar.getInstance()
    var calendar = Calendar.getInstance()

    val pickerDate = Calendar.getInstance()

    private var eventDay by Delegates.notNull<Int>()
    private var eventMonth by Delegates.notNull<Int>()
    private var eventYear by Delegates.notNull<Int>()


    private lateinit var sDate: Date
    private lateinit var eDate: Date

    lateinit var notificationTime:Date
    private lateinit var notificationEndTime:Date
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_calendar_add_event, container, false)
        binding = DataBindingUtil.inflate<FragmentCalendarAddEventBinding>(
            inflater,
            layout.fragment_calendar_add_event,
            container,
            false
        )


        // Instantiating viewmodel using factory class
        val calendarAddItemViewModelProvider =
            CalendarAddEventViewModelFactory(getDatabase(requireActivity().application),requireContext())
        viewModel = ViewModelProvider(
            this@CalendarAddEventFragment,
            calendarAddItemViewModelProvider
        )[CalendarAddEventViewModel::class.java]

        // Creating toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.topAppBar)
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        binding.topAppBar.setupWithNavController(findNavController(), appBarConfiguration)

        val arg=CalendarAddEventFragmentArgs.fromBundle(requireArguments())
        eventDay=arg.day
        eventMonth=arg.month
        eventYear=arg.year

        Timber.d("${arg.day} ${arg.month} ${arg.year}")

        calendar.set(eventYear,eventMonth-1,eventDay)
        binding.startDateEdit.text =
            SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time)
        binding.endDateEdit.text =
            SimpleDateFormat("MM/dd/yyyy", Locale.US).format(calendar.time)
        sDate = calendar.time
        eDate=calendar.time
        //continue from here...

        createNotificationChannel()

        //Start date
        val startDateListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                startCalendar.set(p1, p2, p3)
                binding.startDateEdit.text =
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(startCalendar.time)
                binding.endDateEdit.text =
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(startCalendar.time)
                sDate = startCalendar.time
                eDate=startCalendar.time
            }
        }

        binding.startDateEdit.setOnClickListener {
            DatePickerDialog(
                requireContext(), startDateListener, startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH),
                startCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //End Date
        val endDateListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                endCalendar.set(p1, p2, p3)

                //binding.startDateEdit.text=SimpleDateFormat("MM/dd/yyyy",Locale.US).format(calendar.time)
                //Toast.makeText(requireContext(),"calendar time ${calendar.time}",Toast.LENGTH_SHORT).show()
                Log.d("Manasa", "endDate: ${calendar.time.time}")
                binding.endDateEdit.text =
                    SimpleDateFormat("MM/dd/yyyy", Locale.US).format(endCalendar.time)
                endCalendar.add(Calendar.DATE, 1)
                eDate = endCalendar.time
            }
        }

        binding.endDateEdit.setOnClickListener {
            DatePickerDialog(
                requireContext(), endDateListener, endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH),
                endCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        //Start Time
        val startTimeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                stCalendar.set(sDate.year+1900, sDate.month,sDate.date , p1, p2)
                binding.startTimeEdit.text =
                    SimpleDateFormat("HH:mm", Locale.US).format(stCalendar.time)
               // Log.d("Manasa"," notification time ${sDate} and ${calendar.time} and  ${sDate.year} ${sDate.month} ${sDate.date}")
                notificationTime=stCalendar.time
                stCalendar.set(0, 0, 0, p1 + 1, p2)
                binding.endTimeEdit.text =
                    SimpleDateFormat("HH:mm", Locale.US).format(stCalendar.time)

            }
        }

        binding.startTimeEdit.setOnClickListener {
            TimePickerDialog(
                requireContext(), startTimeListener, stCalendar.get(Calendar.HOUR),
                stCalendar.get(Calendar.MINUTE), true
            ).show()
        }


        //End time
        val endTimeListener = object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                etCalendar.set(eDate.year, eDate.month, eDate.date, p1, p2)
                notificationEndTime=etCalendar.time
                binding.endTimeEdit.text =
                    SimpleDateFormat("HH:mm", Locale.US).format(etCalendar.time)
            }
        }

        binding.endTimeEdit.setOnClickListener {
            TimePickerDialog(
                requireContext(), endTimeListener, etCalendar.get(Calendar.HOUR),
                etCalendar.get(Calendar.MINUTE), true
            ).show()
        }

        //save
        binding.save.setOnClickListener {
            closeKeyboard()
            if (checkTitleNotEmpty() && checkStartTimeNotEmpty() && checkStartDateNotEmpty()) {
                val title = binding.title.text.toString().trim()
                val location = binding.place.text.toString().trim()
                val startDate = sDate
                  //java.sql.Date.valueOf(binding.startDateEdit.text.toString().trim())
                val startTime = binding.startTimeEdit.text.toString().trim()
                val endDate = eDate
                //java.sql.Date.valueOf(binding.endDateEdit.text.toString().trim())
                val endTime = binding.endTimeEdit.text.toString().trim()
                val description = binding.detail.text.toString().trim()
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
                viewModel.updateCurrentTime(notificationTime.time)
                clearValues()
                //getDateScheduleNotification()


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
            .apply { setShowBadge(false) }
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.description = desc
        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
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
        binding.title.text.let {
            return if (binding.title.text?.isEmpty() == true) {
                binding.titleEdit.error = getString(string.error_empty_title)
                false
            } else
                true

        }
    }

    //checks if starttime is not empty
    private fun checkStartTimeNotEmpty(): Boolean {
        binding.startTimeEdit.text.let {
            return if (binding.startTimeEdit.text?.isEmpty() == true) {
                binding.startTimeEdit.error = getString(string.error_empty_startTime)
                false
            } else
                true
        }
    }

    //checks if startdate is not empty
    private fun checkStartDateNotEmpty(): Boolean {
        binding.startDateEdit.text.let {
            return if (binding.startDateEdit.text?.isEmpty() == true) {
                binding.startDateEdit.error = getString(string.error_empty_startDate)
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