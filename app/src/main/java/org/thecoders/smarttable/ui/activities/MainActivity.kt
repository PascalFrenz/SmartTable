package org.thecoders.smarttable.ui.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_main.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.ui.adapters.ExamAdapter
import org.thecoders.smarttable.ui.adapters.HomeworkAdapter
import org.thecoders.smarttable.ui.dialogs.*
import org.thecoders.smarttable.ui.fragments.ExamlistFragment
import org.thecoders.smarttable.ui.fragments.HomeworklistFragment
import org.thecoders.smarttable.ui.fragments.TimetableFragment
import org.thecoders.smarttable.viewmodel.ExamViewModel
import org.thecoders.smarttable.viewmodel.HomeworkViewModel
import org.thecoders.smarttable.viewmodel.SubjectViewModel

class MainActivity : AppCompatActivity(),
        TimetableFragment.OnSubjectActionRequest,
        ExamAdapter.OnExamEditClickListener,
        HomeworkAdapter.OnHomeworkEditClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {


    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    private lateinit var mSectionsPagerAdapter: SectionsPagerAdapter
    private var mCurrentPage: Int = 0
    private var mLastPage: Int = 0

    private var mSharedFab: FloatingActionButton? = null

    private lateinit var mSubjectViewModel: SubjectViewModel
    private lateinit var mExamViewModel: ExamViewModel
    private lateinit var mHomeworkViewModel: HomeworkViewModel


    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
        private val STATE_PAGE_ID = "currentPageID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_TAG, "onCreate-Method toggled!")

        supportActionBar?.title = "Smart Table"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.elevation = 0f

        setContentView(R.layout.activity_main)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)
        mExamViewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)
        mHomeworkViewModel = ViewModelProviders.of(this).get(HomeworkViewModel::class.java)

        if (savedInstanceState != null) {
            mCurrentPage = savedInstanceState.getInt(STATE_PAGE_ID)
            mLastPage = mCurrentPage
            Log.v(LOG_TAG, "SavedInstanceState was not null")
        } else {
            mCurrentPage = 1
            mLastPage = mCurrentPage
            Log.v(LOG_TAG, "SavedInstanceState was null")
        }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        mSharedFab = activity_main_fab
        mSharedFab?.visibility = View.INVISIBLE     //Making the fab invisible here to ensure it
                                                    //is not shown at startup

        // Set up the ViewPager with the sections adapter.
        activity_main_viewpager.adapter = mSectionsPagerAdapter
        activity_main_viewpager.currentItem = mCurrentPage
        activity_main_viewpager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                when(state) {
                    ViewPager.SCROLL_STATE_DRAGGING -> {
                        if(mSharedFab?.isShown as Boolean) mSharedFab?.hide()
                        else { }
                    }
                    ViewPager.SCROLL_STATE_IDLE -> {
                        when(activity_main_viewpager.currentItem) {
                            0 -> {
                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 0) as ExamlistFragment)
                                        .shareFab(mSharedFab)
                                mSharedFab?.show()
                            }
                            1 -> {
                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 0) as ExamlistFragment)
                                        .shareFab(null)

                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 2) as HomeworklistFragment)
                                        .shareFab(null)

                                if(mSharedFab?.isShown as Boolean) mSharedFab?.hide()
                            }
                            2 -> {
                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 2) as HomeworklistFragment)
                                        .shareFab(mSharedFab)
                                mSharedFab?.show()
                            }
                        }
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

            }

        })
        activity_main_tablayout.setupWithViewPager(activity_main_viewpager)
        activity_main_tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mCurrentPage = tab.position
                activity_main_viewpager.currentItem = mCurrentPage
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {  }

            override fun onTabReselected(tab: TabLayout.Tab) {  }
        })
    }

    public override fun onStart() {
        super.onStart()
        if (activity_main_viewpager.adapter == null) {
            activity_main_viewpager.adapter = mSectionsPagerAdapter
        }
        Log.v(LOG_TAG, "onStart-Method toggled!")
    }

    public override fun onPause() {
        super.onPause()
        Log.v(LOG_TAG, "onPause-Method toggled!")
        mLastPage = mCurrentPage
    }

    public override fun onResume() {
        super.onResume()
        Log.v(LOG_TAG, "onResume-Method toggled!")
        activity_main_viewpager.currentItem = mLastPage
    }

    public override fun onStop() {
        super.onStop()
        Log.v(LOG_TAG, "onStop-Method toggled!")
        if (activity_main_viewpager.adapter != null)
            activity_main_viewpager.adapter = null
    }

    override fun onDestroy() {
        super.onDestroy()
        AppDatabase.destroyInstance()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.v(LOG_TAG, "onSaveInstanceState-Method toggled!")
        outState.putInt(STATE_PAGE_ID, mCurrentPage)
    }

    public override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.v(LOG_TAG, "onRestoreInstanceState-Method toggled!")
        if (savedInstanceState != null)
            mCurrentPage = savedInstanceState.getInt(STATE_PAGE_ID)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSubjectActionRequest() {
        SubjectActionsDialog.newInstance().show(fragmentManager, "SubjectActionsDialog")
    }

    private lateinit var mEditExamDialog: EditExamDialog

    override fun onExamEditRequest(exam: Exam) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("editExamDialog")
        if(prev != null)
            ft.remove(prev)
        ft.addToBackStack(null)

        //Pack the exam that should be edited into a bundle
        val bundle = Bundle()
        bundle.putLong(Exam.ID, exam.id)
        bundle.putString(Exam.SUBJECT, exam.subject)
        bundle.putString(Exam.TOPIC, exam.topic)
        bundle.putSerializable(Exam.DATE, exam.date)
        bundle.putString(Exam.GRADE, exam.grade)

        //Note: Bundle is passed here!
        mEditExamDialog = EditExamDialog.newInstance(bundle,mSubjectViewModel, mExamViewModel)
        mEditExamDialog.show(ft, "editExamDialog")
    }

    private lateinit var mEditHomeworkDialog: EditHomeworkDialog

    override fun onHomeworkEditRequest(homework: Homework) {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("editHomeworkFragment")
        if (prev != null)
            ft.remove(prev)
        ft.addToBackStack(null)

        val bundle = Bundle()
        bundle.putLong(Homework.ID, homework.id)
        bundle.putString(Homework.SUBJECT, homework.subject)
        bundle.putString(Homework.TASK, homework.task)
        bundle.putSerializable(Homework.START, homework.date_start)
        bundle.putSerializable(Homework.DEADLINE, homework.date_deadline)
        bundle.putBoolean(Homework.FINISHED, homework.finished)
        bundle.putString(Homework.EFFORT, homework.effort)

        mEditHomeworkDialog = EditHomeworkDialog.newInstance(bundle, mSubjectViewModel, mHomeworkViewModel)
        mEditHomeworkDialog.show(ft, "editHomeworkFragment")
    }

    override fun onTimeSet(timePicker: TimePicker, hour: Int, minute: Int) {
        when (timePicker.tag) {
            "EditHomework_timeEffortDialog" ->
                mEditHomeworkDialog.mEffort
                        .setText(MyTimePickerDialog.getTimeSet(hour, minute))
        }
    }

    override fun onDateSet(datePicker: DatePicker, year: Int, month: Int, day: Int) {
        when (datePicker.tag) {
            "EditExam_datePicker" ->
                mEditExamDialog.mDate
                        .setText(MyDatePickerDialog.getDateSet(day, month, year))

            "EditHomework_dateStartDialog" ->
                mEditHomeworkDialog.mDateStart
                        .setText(MyDatePickerDialog.getDateSet(day, month, year))

            "EditHomework_dateDeadlineDialog" ->
                mEditHomeworkDialog.mDeadline
                        .setText(MyDatePickerDialog.getDateSet(day, month, year))
        }
    }





    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> ExamlistFragment.newInstance(mExamViewModel)
                1 -> TimetableFragment()
                2 -> HomeworklistFragment.newInstance(mHomeworkViewModel)
                else -> throw Exception(
                        "Something went wrong while loading fragments... position out of bounds?")
            }

        override fun getCount(): Int = 3

        override fun getPageTitle(position: Int): CharSequence =
            when (position) {
                0 -> "Exams"
                1 -> "Timetable"
                2 -> "Homework"
                else -> "Error"
            }

    }
}