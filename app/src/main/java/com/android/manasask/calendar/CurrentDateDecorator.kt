package com.android.manasask.calendar

import android.text.style.RelativeSizeSpan

import android.graphics.Typeface

import android.text.style.StyleSpan

import android.text.style.ForegroundColorSpan

import com.prolificinteractive.materialcalendarview.DayViewFacade

import com.prolificinteractive.materialcalendarview.CalendarDay

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color

import android.graphics.drawable.Drawable

import com.prolificinteractive.materialcalendarview.DayViewDecorator



class CurrentDateDecorator(context: Context) : DayViewDecorator {
   // private val highlightDrawable: Drawable
    private val context: Context
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == CalendarDay.today()
    }

    override fun decorate(view: DayViewFacade) {
        //view.setBackgroundDrawable(highlightDrawable)
        view.addSpan(ForegroundColorSpan(Color.WHITE))
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(RelativeSizeSpan(1.5f))
    }

    init {
        this.context = context
        //highlightDrawable = this.context.getDrawable(R.color.holo_blue_light)!!
    }
}