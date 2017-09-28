package org.thecoders.smarttable.ui.fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
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
import org.thecoders.smarttable.ui.activities.ModifyDayActivity
import org.thecoders.smarttable.ui.adapters.LessonAdapter
import org.thecoders.smarttable.viewmodel.LessonViewModel


/**
 *
 */

class TimetableFragment : Fragment() {

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
    private lateinit var mLessonAdapter: LessonAdapter

    //TODO: Add all days
    private lateinit var mondayLessons: MutableList<Lesson>
    private lateinit var tuesdayLessons: MutableList<Lesson>
    private lateinit var wednesdayLessons: MutableList<Lesson>
    private lateinit var thursdayLessons: MutableList<Lesson>
    private lateinit var fridayLessons: MutableList<Lesson>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(LessonViewModel::class.java)
        setupDataObservers()

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

        mLessonAdapter = LessonAdapter(activity, mutableListOf(), false)

        mLessonListView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mLessonListView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
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
        val intent = Intent(context, ModifyDayActivity::class.java)
        intent.putExtra("day", "monday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_tue)
    fun onClickTue() {
        mLessonAdapter.alterItems(tuesdayLessons)
    }

    @OnLongClick(R.id.timetable_tue)
    fun onLongClickTue(): Boolean {
        val intent = Intent(context, ModifyDayActivity::class.java)
        intent.putExtra("day", "tuesday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_wed)
    fun onClickWed() {
        mLessonAdapter.alterItems(wednesdayLessons)
    }

    @OnLongClick(R.id.timetable_wed)
    fun onLongClickWed(): Boolean {
        val intent = Intent(context, ModifyDayActivity::class.java)
        intent.putExtra("day", "wednesday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_thu)
    fun onClickThu() {
        mLessonAdapter.alterItems(thursdayLessons)
    }

    @OnLongClick(R.id.timetable_thu)
    fun onLongClickThu(): Boolean {
        val intent = Intent(context, ModifyDayActivity::class.java)
        intent.putExtra("day", "thursday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_fri)
    fun onClickFri() {
        mLessonAdapter.alterItems(fridayLessons)
    }

    @OnLongClick(R.id.timetable_fri)
    fun onLongClickFri(): Boolean {
        val intent = Intent(context, ModifyDayActivity::class.java)
        intent.putExtra("day", "friday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_more)
    fun onClickMore() {
        mCallback.onSubjectActionRequest()
    }

    private fun setupDataObservers() {
        mViewModel.mondayLessons.observe(this, Observer {
            if (it != null) {
                mondayLessons = (it.filter { it.mon != "NULL" }).toMutableList()
            }
        })

        mViewModel.tuesdayLessons.observe(this, Observer {
            if(it != null) {
                tuesdayLessons = (it.filter { it.tue != "NULL" }).toMutableList()
            }
        })

        mViewModel.wednesdayLessons.observe(this, Observer {
            if(it != null) {
                wednesdayLessons = (it.filter { it.wed != "NULL" }).toMutableList()
            }
        })

        mViewModel.thursdayLessons.observe(this, Observer {
            if(it != null) {
                thursdayLessons = (it.filter { it.thu != "NULL" }).toMutableList()
            }
        })

        mViewModel.fridayLessons.observe(this, Observer {
            if(it != null) {
                fridayLessons = (it.filter { it.fri != "NULL" }).toMutableList()
            }
        })
    }
}
