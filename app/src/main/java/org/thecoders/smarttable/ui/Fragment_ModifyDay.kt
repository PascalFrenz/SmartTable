package org.thecoders.smarttable.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.ClipData
import android.content.ClipDescription
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.RearrangeableListView
import org.thecoders.smarttable.TimeHelper
import org.thecoders.smarttable.data.Lesson
import org.thecoders.smarttable.viewmodel.LessonViewModel
import org.thecoders.smarttable.viewmodel.SubjectViewModel


/**
 * Created by frenz on 30.07.2017.
 */

class Fragment_ModifyDay : Fragment() {

    private val LOG_TAG = Fragment_ModifyDay::class.java.simpleName

    private lateinit var mUnbinder: Unbinder
    private lateinit var mSubjectViewModel: SubjectViewModel
    private lateinit var mLessonViewModel: LessonViewModel

    private lateinit var mDay: String

    @BindView(R.id.modifyday_listview) lateinit var mListView: RearrangeableListView
    @BindView(R.id.modifyday_subjectbar) lateinit var mSubjectBar: RecyclerView

    companion object {
        private lateinit var mLessonAdapter: Adapter_Lesson
        private lateinit var mSubjectButtonAdapter: Adapter_SubjectButtons
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)
        mLessonViewModel = ViewModelProviders.of(this).get(LessonViewModel::class.java)
        mDay = activity.intent.getStringExtra("day")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_modify_day, container, false) as View

        mUnbinder = ButterKnife.bind(this, rootView)

        mLessonAdapter = Adapter_Lesson(
                context = activity,
                layoutResourceId = R.layout.listview_item_lesson,
                data = mutableListOf(),
                enableEdit = true
        )

        LessonViewModel.Companion.LoadLessonsIntoAdapter(mLessonAdapter, mDay, mLessonViewModel).execute()
        mListView.adapter = mLessonAdapter

        mSubjectButtonAdapter = Adapter_SubjectButtons(data = mutableListOf())

        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mSubjectBar.layoutManager = mLayoutManager

        LoadSubjectList(mSubjectViewModel).execute()
        mSubjectBar.adapter = mSubjectButtonAdapter

        initListView()

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    private fun initListView() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val startOfDay = preferences.getString(Fragment_Settings.ID_DAY_START, "")
        val lessonDuration = TimeHelper().getMinutes(preferences.getString(Fragment_Settings.ID_LESSON_LENGTH, ""))

        mListView.setRearrangeEnabled(true)
        mListView.rearrangeListener = mLessonAdapter
        mListView.setOnDragListener { _, event ->
            when(event.action) {
                DragEvent.ACTION_DROP -> {
                    val clipData = event.clipData
                    val subject = clipData.description.label.toString()

                    when {
                        mLessonAdapter.data.isNotEmpty() -> {
                            val previousLesson = mLessonAdapter.getItem(mLessonAdapter.count - 1)
                            val newTiming = TimeHelper().calcTimingById(context, previousLesson.id.toInt())

                            mLessonAdapter.add(Lesson(previousLesson.id + 1, newTiming, subject))
                        }
                        mLessonAdapter.data.isEmpty() -> {
                            val lessonStartAndEnd = listOf(
                                    startOfDay,
                                    TimeHelper().addTime(startOfDay, lessonDuration)
                            )

                            val timing = TimeHelper().buildTiming(lessonStartAndEnd)
                            mLessonAdapter.add(Lesson(
                                    id = 0,
                                    timing = timing,
                                    subjectName = subject
                            ))
                        }
                        else -> Log.v(LOG_TAG, "Encountered negative size of array. Exiting")
                    }
                }
            }
            true
        }
    }




    class Adapter_SubjectButtons(var data: MutableList<String>)
        : RecyclerView.Adapter<Adapter_SubjectButtons.SubjectViewHolder>() {

        class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(subjectName: String) {
                val mButton = itemView.findViewById<Button>(R.id.item_subjectbutton_button)
                mButton.text = subjectName
                mButton.setOnLongClickListener {
                    it.tag = subjectName

                    val item = ClipData.Item(it.tag.toString())
                    val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    val data = ClipData(it.tag.toString(), mimeTypes, item)

                    val shadowBuilder = View.DragShadowBuilder(it)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        it.startDragAndDrop(data, shadowBuilder, null, 0)
                    } else {
                        @Suppress("DEPRECATION")
                        it.startDrag(data, shadowBuilder, null, 0)
                    }
                    true
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
            val button = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listview_item_subject_button, parent, false)

            return SubjectViewHolder(button)
        }

        override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
            holder.bind(data[position])
        }

        override fun getItemCount(): Int {
            return data.count()
        }
    }

    class LoadSubjectList(private val subjectViewModel: SubjectViewModel) : AsyncTask<String, Int, List<String>>() {
        override fun doInBackground(vararg params: String?): List<String> =
                subjectViewModel.loadSubjectNames()

        override fun onPostExecute(result: List<String>) {
            mSubjectButtonAdapter.data = result as MutableList<String>
            mSubjectButtonAdapter.notifyDataSetChanged()
        }
    }
}