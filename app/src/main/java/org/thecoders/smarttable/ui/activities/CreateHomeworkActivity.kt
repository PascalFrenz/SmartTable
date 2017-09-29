package org.thecoders.smarttable.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_create_homework.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.ui.dialogs.MyDatePickerDialog
import org.thecoders.smarttable.ui.dialogs.MyTimePickerDialog
import org.thecoders.smarttable.ui.fragments.CreateHomeworkFragment
import org.thecoders.smarttable.viewmodel.HomeworkViewModel

class CreateHomeworkActivity : AppCompatActivity(),
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener,
        CreateHomeworkFragment.OnAddHomeworkPressedListener {


    private lateinit var mFragment: CreateHomeworkFragment
    private lateinit var mHomeworkViewModel: HomeworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_homework)

        mHomeworkViewModel = ViewModelProviders.of(this).get(HomeworkViewModel::class.java)

        if(activity_createhomework_content != null) {

            if(savedInstanceState != null)
                return

            mFragment = CreateHomeworkFragment()
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
        mFragment.mEffort.setText(MyTimePickerDialog.getTimeSet(hourOfDay, minute))
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mFragment.mDeadline.setText(MyDatePickerDialog.getDateSet(dayOfMonth, month, year))
    }


}
