package com.android.manasask.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.manasask.calendar.databinding.FragmentCalendarMainBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= DataBindingUtil.inflate<FragmentCalendarMainBinding>(inflater,R.layout.fragment_calendar_main,container,false)

        binding.apply {
            calendarView.isDynamicHeightEnabled=true
            calendarView.selectedDate= CalendarDay.today()


            tvDatePickerText.text=Utils.getTitleText(CalendarDay.today())
        }

        return binding.root
    }


}