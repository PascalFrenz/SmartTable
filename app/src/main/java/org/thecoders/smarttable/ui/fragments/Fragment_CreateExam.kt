package org.thecoders.smarttable.ui.fragments

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.viewmodel.SubjectViewModel

/**
 * Created by frenz on 02.07.2017.
 */

class Fragment_CreateExam : LifecycleFragment() {

    interface OnAddExamPressedListener {
        fun onAddExamRequested(exam: Exam)
    }

    private lateinit var mSubjectViewModel: SubjectViewModel
    private lateinit var mLessonSubjectAdapter: ArrayAdapter<String>
    private lateinit var mSubjects: List<String>

    @BindView(R.id.createexam_subject) lateinit var mSubject: Spinner
    @BindView(R.id.createexam_topic) lateinit var mTopic: EditText
    @BindView(R.id.createexam_date) lateinit var mDate: EditText
    @BindView(R.id.createexam_date_btn) lateinit var mDateButton: Button
    @BindView(R.id.createexam_add_fab) lateinit var mAddFab: FloatingActionButton

    private lateinit var mUnbinder: Unbinder
    private lateinit var mCallback: OnAddExamPressedListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)

        mCallback = try {
            activity as OnAddExamPressedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + "must implement OnAddExamPressedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater?.inflate(R.layout.fragment_create_exam, container, false)!!
        mUnbinder = ButterKnife.bind(this, rootView)

        mLessonSubjectAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                mutableListOf()
        )

        mSubject.adapter = mLessonSubjectAdapter

        mSubjectViewModel.subjectNamesList.observe(this, Observer {
            if(it != null) {
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

    @OnClick(R.id.createexam_add_fab)
    fun addExamToDatabase() {
        if(mTopic.text.toString() == "" || mDate.text.toString()  == "") {

            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        } else {
            val df = DateConverter.dateFormat

            val newExam = Exam(
                    id = 0,
                    subject = mSubject.selectedItem.toString().trim(),
                    topic = mTopic.text.toString().trim(),
                    date = df.parse(mDate.text.toString().trim())
            )

            mCallback.onAddExamRequested(newExam)
        }
    }

    @OnClick(R.id.createexam_date_btn)
    fun showDatePicker() = Fragment_DatePicker().show(activity.fragmentManager, "datePicker")

}