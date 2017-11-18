package org.thecoders.smarttable.ui.activities

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_exam.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.ui.dialogs.MyDatePickerDialog
import org.thecoders.smarttable.ui.fragments.CreateExamFragment
import org.thecoders.smarttable.viewmodel.ExamViewModel

/**
 * Created by frenz on 02.07.2017.
 *
 * Host-Activity for CreateExamFragment.
 * Also contains a reference to the database via mExamViewModel.
 *
 * @see ExamViewModel
 */

class CreateExamActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener, CreateExamFragment.OnAddExamPressedListener {

    /**
     * Contains all UI elements that the user interact with.
     */
    private lateinit var mFragment: CreateExamFragment

    /**
     * Provides an interface for communicating with the database.
     */
    private lateinit var mExamViewModel: ExamViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exam)

        mExamViewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)

        if(activity_createexam_content  != null) {

            if(savedInstanceState != null)
                return

            mFragment = CreateExamFragment()
            mFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_createexam_content, mFragment)
                    .commit()
        }
    }

    override fun onDateSet(view: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mFragment.mDate.setText(MyDatePickerDialog.getDateSet(dayOfMonth, month, year))
    }


    override fun onAddExamRequested(exam: Exam) {
        mExamViewModel.addExam(exam)
        NavUtils.navigateUpFromSameTask(this)
    }
}