package org.thecoders.smarttable.ui.fragments

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Lesson
import org.thecoders.smarttable.helpers.LessonItemTouchHelperCallback
import org.thecoders.smarttable.helpers.TimeHelper
import org.thecoders.smarttable.ui.activities.Activity_ModifyDay
import org.thecoders.smarttable.ui.adapters.Adapter_Lesson
import org.thecoders.smarttable.ui.adapters.Adapter_SubjectButtonBar
import org.thecoders.smarttable.viewmodel.LessonViewModel
import org.thecoders.smarttable.viewmodel.SubjectViewModel


/**
 * Created by frenz on 30.07.2017.
 */

class Fragment_ModifyDay : LifecycleFragment(), Activity_ModifyDay.SaveDayListener {

    private val LOG_TAG = Fragment_ModifyDay::class.java.simpleName

    private lateinit var mUnbinder: Unbinder

    private lateinit var mSubjectViewModel: SubjectViewModel
    private lateinit var mLessonViewModel: LessonViewModel

    private lateinit var mSubjectButtonAdapter: Adapter_SubjectButtonBar
    private lateinit var mLessonAdapter: Adapter_Lesson

    private lateinit var mDay: String
    private var overallTimetableItemNumber = 0

    @BindView(R.id.modifyday_listview) lateinit var mLessonListView: RecyclerView
    @BindView(R.id.modifyday_subjectbar) lateinit var mSubjectBar: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)
        mLessonViewModel = ViewModelProviders.of(this).get(LessonViewModel::class.java)

        mDay = activity.intent.getStringExtra("day")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_modify_day, container, false) as View

        mUnbinder = ButterKnife.bind(this, rootView)

        mLessonAdapter = Adapter_Lesson(activity, mutableListOf(), true)
        mSubjectButtonAdapter = Adapter_SubjectButtonBar(data = mutableListOf())

        initLessonListView()
        initButtonBar()

        setupViewModelObservers()

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    override fun saveDay() {
        val data = mLessonAdapter.data

        if(data.isNotEmpty()) {
            if(data[0].id.toInt() == 0)
                data.forEach { it.id++ }
        }


        AsyncTask.execute {
            var dataLength = data.size

            while (dataLength < overallTimetableItemNumber) {
                dataLength++
                data.add(Lesson(dataLength.toLong()))
            }

            data.forEach {
                mLessonViewModel.insertOrUpdateLesson(it, mDay)
                Log.v(LOG_TAG, it.toString())
            }
        }

    }

    private fun initLessonListView() {
        val timetableLayoutManager = LinearLayoutManager(context)
        timetableLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val dividerItemDecoration = DividerItemDecoration(context, timetableLayoutManager.orientation)

        mLessonListView.layoutManager = timetableLayoutManager
        mLessonListView.addItemDecoration(dividerItemDecoration)
        mLessonListView.adapter = mLessonAdapter


        //TODO: Use Dagger 2 to inject preferences
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val startOfDay = preferences.getString(Fragment_Settings.ID_DAY_START, "")
        val lessonDuration = TimeHelper.getMinutes(preferences.getString(Fragment_Settings.ID_LESSON_LENGTH, ""))

        val callback = LessonItemTouchHelperCallback(mLessonAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(mLessonListView)

        mLessonListView.setOnDragListener { _, event ->
            when(event.action) {
                DragEvent.ACTION_DROP -> {
                    Log.v(LOG_TAG, "Called!")
                    val clipData = event.clipData
                    val subject = clipData.description.label.toString()

                    when {
                        mLessonAdapter.data.isNotEmpty() -> {
                            val previousLesson = mLessonAdapter.data.last()
                            val newTiming = TimeHelper.calcTimingById(context, previousLesson.id.toInt())
                            val newLesson = Lesson(previousLesson.id + 1, newTiming, subject)

                            mLessonAdapter.data.add(newLesson)
                            mLessonAdapter.notifyItemInserted(newLesson.id.toInt())
                        }
                        mLessonAdapter.data.isEmpty() -> {
                            val lessonStartAndEnd = listOf(
                                    startOfDay,
                                    TimeHelper.addTime(startOfDay, lessonDuration)
                            )

                            val timing = TimeHelper.buildTiming(lessonStartAndEnd)
                            mLessonAdapter.data.add(Lesson(
                                    id = 1,
                                    timing = timing,
                                    subjectName = subject
                            ))
                            mLessonAdapter.notifyItemInserted(0)
                        }
                        else -> Log.v(LOG_TAG, "Encountered negative size of array. Exiting")
                    }
                }
            }
            true
        }
    }

    private fun initButtonBar() {
        val buttonbarLayoutManager = LinearLayoutManager(context)
        buttonbarLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        mSubjectBar.layoutManager = buttonbarLayoutManager
        mSubjectBar.adapter = mSubjectButtonAdapter
    }

    private fun setupViewModelObservers() {
        when(mDay) {
            "monday" ->
                mLessonViewModel.mondayLessons.observe(this, Observer {
                    if (it != null) {
                        mLessonAdapter.alterItems(it.filter { it.mon != "NULL" })
                        overallTimetableItemNumber = it.size
                    }
                })
            else -> {}
        }

        mSubjectViewModel.subjectNamesList.observe(this, Observer {
            if (it != null) {
                mSubjectButtonAdapter.alterItems(it)
            }
        })
    }
}