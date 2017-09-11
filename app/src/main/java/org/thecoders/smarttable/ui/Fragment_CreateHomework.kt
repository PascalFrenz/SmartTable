package org.thecoders.smarttable.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.AsyncTask
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
import org.thecoders.smarttable.data.Homework
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

    companion object {
        private lateinit var mLessonSubjectAdapter: ArrayAdapter<String>
    }

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

        mLessonSubjectAdapter = ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                mutableListOf()
        )

        LoadSubjectNames(mSubjectViewModel).execute()

        mSubject.adapter = mLessonSubjectAdapter

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
    fun showTimePicker() = Fragment_TimePicker().show(activity.fragmentManager, "timePicker")

    @OnClick(R.id.createhomework_deadline_btn)
    fun showDatePicker() = Fragment_DatePicker().show(activity.fragmentManager, "datePicker")


    //Used to load a list of lesson subjects as strings in an async way and then add them to an
    //adapter
    class LoadSubjectNames(private val subjectViewModel: SubjectViewModel) : AsyncTask<String, Int, List<String>>() {

        override fun doInBackground(vararg params: String?): List<String>
                = subjectViewModel.loadSubjectNames()

        override fun onPostExecute(result: List<String>?) {
            if(result != null) {
                mLessonSubjectAdapter.clear()
                for (subject in result) mLessonSubjectAdapter.add(subject)
            }
        }
    }
}
