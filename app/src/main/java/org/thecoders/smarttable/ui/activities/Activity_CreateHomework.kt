package org.thecoders.smarttable.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.DatePicker
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_create_homework.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.ui.dialogs.Dialog_DatePicker
import org.thecoders.smarttable.ui.dialogs.Dialog_TimePicker
import org.thecoders.smarttable.ui.fragments.Fragment_CreateHomework
import org.thecoders.smarttable.viewmodel.HomeworkViewModel

class Activity_CreateHomework : AppCompatActivity(),
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        Fragment_CreateHomework.OnAddHomeworkPressedListener {


    private lateinit var mFragment: Fragment_CreateHomework
    private lateinit var mHomeworkViewModel: HomeworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_homework)

        mHomeworkViewModel = ViewModelProviders.of(this).get(HomeworkViewModel::class.java)

        if(activity_createhomework_content != null) {

            if(savedInstanceState != null)
                return

            mFragment = Fragment_CreateHomework()
            mFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_createhomework_content, mFragment)
                    .commit()
        }
    }

    override fun onAddHomeworkRequested(homework: Homework) {
        mHomeworkViewModel.addHomework(homework)
        NavUtils.navigateUpFromSameTask(this)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        mFragment.mEffort.setText(Dialog_TimePicker.getTimeSet(hourOfDay, minute))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mFragment.mDeadline.setText(Dialog_DatePicker.getDateSet(dayOfMonth, month, year))
    }


}
