package org.thecoders.smarttable.ui

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker

/**
 * Created by Pascal on 26.10.2016.
 * The dialog that shows up when the user is supposed to pick a time.
 */

class Fragment_TimePicker : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val hour = 0
        val minute = 0

        return TimePickerDialog(activity, this, hour, minute, true)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        (activity as TimePickerDialog.OnTimeSetListener)
                .onTimeSet(view, hourOfDay, minute)
    }

    companion object {

        fun getTimeSet(hour: Int, minute: Int): String {
            val sb = StringBuilder(4)

            if (hour < 10) sb.append("0")
            sb.append(hour)
            sb.append(":")
            if (minute < 10) sb.append("0")
            sb.append(minute)
            sb.append("h")


            return sb.toString()       // Output examples: 00:00h ; 01:05h
        }
    }
}
