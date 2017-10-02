package org.thecoders.smarttable.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.widget.DatePicker
import java.util.*

/**
 * Created by Pascal on 26.10.2016.
 * The dialog that shows up when the user is supposed to pick a date.
 */

class MyDatePickerDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        year = c.get(Calendar.YEAR)
        month = c.get(Calendar.MONTH)
        day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity, this, year, month, day)
    }


    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        view.tag = this.tag
        (activity as DatePickerDialog.OnDateSetListener)
                .onDateSet(view, year, monthOfYear + 1, dayOfMonth)
    }

    companion object {

        fun getDateSet(day: Int, month: Int, year: Int): String {
            val sb = StringBuilder(8)

            if (day < 10) sb.append("0")
            sb.append(day)
            sb.append(".")

            if (month < 10) sb.append("0")
            sb.append(month)
            sb.append(".")

            sb.append(year)

            return sb.toString() //Output example: 13.11.2016
        }
    }
}
