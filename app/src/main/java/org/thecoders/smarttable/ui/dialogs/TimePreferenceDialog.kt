package org.thecoders.smarttable.ui.dialogs

import android.content.Context
import android.content.res.TypedArray
import android.os.Build
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.TimePicker
import org.thecoders.smarttable.helpers.TimeHelper

@Suppress("DEPRECATION")
class TimePreferenceDialog(ctxt: Context, attrs: AttributeSet) : DialogPreference(ctxt, attrs) {
    private var lastHour = 0
    private var lastMinute = 0
    private lateinit var picker: TimePicker

    init {
        positiveButtonText = "Set"
        negativeButtonText = "Cancel"
    }

    override fun onCreateDialogView(): View {
        picker = TimePicker(context)
        picker.setIs24HourView(true)
        return picker
    }

    override fun onBindDialogView(v: View) {
        super.onBindDialogView(v)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            picker.hour = lastHour
            picker.minute = lastMinute
        } else {
            picker.currentHour = lastHour
            picker.currentMinute = lastMinute
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)

        if (positiveResult) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                lastHour = picker.hour
                lastMinute = picker.minute
            } else {
                lastHour = picker.currentHour
                lastMinute = picker.currentMinute
            }

            val sb = StringBuilder()

            if (lastHour < 10) sb.append("0")
            sb.append(lastHour)
            sb.append(":")
            if (lastMinute < 10) sb.append("0")
            sb.append(lastMinute)

            val time = sb.toString()

            if (callChangeListener(time)) {
                persistString(time)
            }
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getString(index)
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        val time: String = if (restoreValue) {
            if (defaultValue == null) {
                getPersistedString("00:00")
            } else {
                getPersistedString(defaultValue.toString())
            }
        } else {
            defaultValue!!.toString()
        }

        lastHour = TimeHelper.getHours(time).toInt()
        lastMinute = TimeHelper.getMinutes(time).toInt()
    }
}