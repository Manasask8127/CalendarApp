package com.android.manasask.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.android.manasask.calendar.databinding.FragmentCalendarAddEventBinding
import com.android.manasask.calendar.databinding.FragmentCalendarMainBinding

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_calendar_add_event, container, false)
        val binding=DataBindingUtil.inflate<FragmentCalendarAddEventBinding>(inflater,R.layout.fragment_calendar_add_event,container,false)
        return binding.root
    }



}