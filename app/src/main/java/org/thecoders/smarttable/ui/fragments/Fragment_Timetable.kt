package org.thecoders.smarttable.ui.fragments


import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Lesson
import org.thecoders.smarttable.ui.activities.Activity_ModifyDay
import org.thecoders.smarttable.ui.adapters.Adapter_Lesson
import org.thecoders.smarttable.viewmodel.LessonViewModel


/**
 *
 */

class Fragment_Timetable : LifecycleFragment() {

    interface OnSubjectActionRequest {
        fun onSubjectActionRequest()
    }


    private lateinit var unbinder: Unbinder

    @BindView(R.id.timetable_listview) lateinit var mLessonListView: RecyclerView
    @BindView(R.id.timetable_mon) lateinit var btn_mon: Button
    @BindView(R.id.timetable_tue) lateinit var btn_tue: Button
    @BindView(R.id.timetable_wed) lateinit var btn_wed: Button
    @BindView(R.id.timetable_thu) lateinit var btn_thu: Button
    @BindView(R.id.timetable_fri) lateinit var btn_fri: Button
    @BindView(R.id.timetable_more) lateinit var more: Button

    private lateinit var mCallback: OnSubjectActionRequest

    private lateinit var mViewModel: LessonViewModel
    private lateinit var mLessonAdapter: Adapter_Lesson

    //TODO: Add all days
    private lateinit var mondayLessons: MutableList<Lesson>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(LessonViewModel::class.java)

        mCallback =
                try {
                    activity as OnSubjectActionRequest
                } catch (e: ClassCastException) {
                    throw ClassCastException(activity.toString() + " must implement OnSubjectActionRequest")
                }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_timetable, container, false) as View

        unbinder = ButterKnife.bind(this, rootView)

        mLessonAdapter = Adapter_Lesson(activity, mutableListOf(), false)

        mViewModel.mondayLessons.observe(this, Observer {
            if (it != null) {
                mondayLessons = (it.filter { it.mon != "NULL" }).toMutableList()
            }
        })

        val buttonbarLayoutManager = LinearLayoutManager(context)
        buttonbarLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val dividerItemDecoration = DividerItemDecoration(context, buttonbarLayoutManager.orientation)

        mLessonListView.layoutManager = buttonbarLayoutManager
        mLessonListView.addItemDecoration(dividerItemDecoration)
        mLessonListView.adapter = mLessonAdapter

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.timetable_mon)
    fun onClickMon() {
        mLessonAdapter.alterItems(mondayLessons)
    }

    @OnLongClick(R.id.timetable_mon)
    fun onLongClickMon(): Boolean {
        val intent = Intent(context, Activity_ModifyDay::class.java)
        intent.putExtra("day", "monday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_more)
    fun onClickMore() {
        mCallback.onSubjectActionRequest()
    }
}
