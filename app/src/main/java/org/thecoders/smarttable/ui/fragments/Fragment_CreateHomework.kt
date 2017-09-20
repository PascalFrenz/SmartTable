package org.thecoders.smarttable.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.ui.dialogs.Dialog_DatePicker
import org.thecoders.smarttable.ui.dialogs.Dialog_TimePicker
import org.thecoders.smarttable.viewmodel.SubjectViewModel
import java.util.*

class Fragment_CreateHomework : Fragment() {

    interface OnAddHomeworkPressedListener {
        /**
         * @param homework The Homework object that is supposed to be added to the database
         */
        fun onAddHomeworkRequested(homework: Homework)
    }

    private lateinit var mSubjectViewModel: SubjectViewModel
    private lateinit var mLessonSubjectAdapter: ArrayAdapter<String>
    private lateinit var mSubjects: List<String>

    @BindView(R.id.createhomework_subject) lateinit var mSubject: Spinner
    @BindView(R.id.createhomework_task) lateinit var mTask: EditText
    @BindView(R.id.createhomework_effort) lateinit var mEffort: EditText
    @BindView(R.id.createhomework_deadline) lateinit var mDeadline: EditText
    @BindView(R.id.createhomework_add_fab) lateinit var mAddFab: FloatingActionButton

    private lateinit var mUnbinder: Unbinder
    private lateinit var mCallback: OnAddHomeworkPressedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)

        mCallback = try {
            activity as OnAddHomeworkPressedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "must implement OnAddHomeworkPressedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater?.inflate(R.layout.fragment_create_homework, container, false) as View
        mUnbinder = ButterKnife.bind(this, rootView)

        mLessonSubjectAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                mutableListOf()
        )

        mSubject.adapter = mLessonSubjectAdapter

        mSubjectViewModel.subjectNamesList.observe(this, Observer {
            if (it != null) {
                mSubjects = it
                mLessonSubjectAdapter.clear()
                mLessonSubjectAdapter.addAll(mSubjects)
            }
        })

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    @OnClick(R.id.createhomework_add_fab)
    fun addHomeworkToDatabase() {
        if(mTask.text.toString() == "" || mEffort.text.toString() == "" || mDeadline.text.toString() == "") {

            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()

        } else {
            val df = DateConverter.dateFormat
            mCallback.onAddHomeworkRequested(Homework(
                    id = 0,
                    subject = mSubject.selectedItem.toString().trim(),
                    task = mTask.text.toString().trim(),
                    date_start = Date(),
                    date_deadline = df.parse(mDeadline.text.toString().trim()),
                    effort = mEffort.text.toString().trim(),
                    finished = false
            ))
        }
    }

    @OnClick(R.id.createhomework_effort_btn)
    fun showTimePicker() = Dialog_TimePicker().show(activity.fragmentManager, "timePicker")

    @OnClick(R.id.createhomework_deadline_btn)
    fun showDatePicker() = Dialog_DatePicker().show(activity.fragmentManager, "datePicker")

}
