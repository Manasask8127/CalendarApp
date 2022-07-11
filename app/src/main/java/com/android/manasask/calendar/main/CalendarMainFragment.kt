package com.android.manasask.calendar.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.manasask.calendar.CurrentDateDecorator
import com.android.manasask.calendar.EventAdapter
import com.android.manasask.calendar.R
import com.android.manasask.calendar.Utils.getDate
import com.android.manasask.calendar.databinding.FragmentCalendarMainBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import timber.log.Timber
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarMainFragment : Fragment() {

    private val eventAdapter = EventAdapter()
    private lateinit var calendarMainViewModel: CalendarMainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentCalendarMainBinding>(
            inflater,
            R.layout.fragment_calendar_main,
            container,
            false
        )

        binding.rvCalendar.adapter = eventAdapter
        calendarMainViewModel =
            ViewModelProvider(requireActivity())[CalendarMainViewModel::class.java]



        binding.apply {
            //Calendar view
            calendarView.addDecorator(CurrentDateDecorator(requireContext()))


            //sets to current date
            ivCurrentDate.setOnClickListener {
                calendarView.currentDate = CalendarDay.today()
                calendarView.selectedDate = CalendarDay.today()

            }

            floatingActionButton.setOnClickListener {
                findNavController().navigate(CalendarMainFragmentDirections.actionCalendarMainFragmentToCalendarAddEventFragment())
            }

            calendarView.setOnMonthChangedListener { _, date ->
                calendarMainViewModel.setEventDate(getDate(date))
            }

            calendarView.setOnDateChangedListener { _, date, _ ->
                Timber.d("on date change ${date}")
                // Toast.makeText(requireContext(),"date is ${date}",Toast.LENGTH_SHORT).show()
                calendarMainViewModel.setEventDate(getDate(date))

            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarMainViewModel.eventList.observe(viewLifecycleOwner) { eventsList ->
            Log.d("Manasa ", "fragment List ${eventsList}")
            eventAdapter.submitList(eventsList)
        }
    }


}