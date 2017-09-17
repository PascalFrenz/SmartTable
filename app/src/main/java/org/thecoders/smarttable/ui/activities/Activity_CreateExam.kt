package org.thecoders.smarttable.ui.activities

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_create_exam.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.ui.fragments.Fragment_CreateExam
import org.thecoders.smarttable.ui.fragments.Fragment_DatePicker
import org.thecoders.smarttable.viewmodel.ExamViewModel

/**
 * Created by frenz on 02.07.2017.
 */

class Activity_CreateExam : AppCompatActivity(), DatePickerDialog.OnDateSetListener, Fragment_CreateExam.OnAddExamPressedListener {

    private lateinit var mFragment: Fragment_CreateExam
    private lateinit var mExamViewModel: ExamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exam)

        mExamViewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)

        if(activity_createexam_content  != null) {

            if(savedInstanceState != null)
                return

            mFragment = Fragment_CreateExam()
            mFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_createexam_content, mFragment)
                    .commit()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mFragment.mDate.setText(Fragment_DatePicker.getDateSet(dayOfMonth, month, year))
    }

    override fun onAddExamRequested(exam: Exam) {
        mExamViewModel.addExam(exam)
        NavUtils.navigateUpFromSameTask(this)
    }
}