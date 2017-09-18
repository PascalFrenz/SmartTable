package org.thecoders.smarttable.ui.activities

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
import kotlinx.android.synthetic.main.activity_main.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.ui.fragments.Fragment_Examlist
import org.thecoders.smarttable.ui.fragments.Fragment_Homeworklist
import org.thecoders.smarttable.ui.fragments.Fragment_Timetable

class Activity_Main : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * [FragmentPagerAdapter] derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var mCurrentPage: Int = 0
    private var mLastPage: Int = 0

    private var mSharedFab: FloatingActionButton? = null

    /**
     * The [ViewPager] that will host the section contents.
     */

    companion object {
        private val LOG_TAG = Activity_Main::class.java.simpleName
        private val STATE_PAGE_ID = "currentPageID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_TAG, "onCreate-Method toggled!")

        supportActionBar?.title = "Smart Table"
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.elevation = 0f

        setContentView(R.layout.activity_main)

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
                                        .instantiateItem(activity_main_viewpager, 0) as Fragment_Examlist)
                                        .shareFab(mSharedFab)
                                mSharedFab?.show()
                            }
                            1 -> {
                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 0) as Fragment_Examlist)
                                        .shareFab(null)

                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 2) as Fragment_Homeworklist)
                                        .shareFab(null)

                                if(mSharedFab?.isShown as Boolean) mSharedFab?.hide()
                            }
                            2 -> {
                                (activity_main_viewpager.adapter
                                        .instantiateItem(activity_main_viewpager, 2) as Fragment_Homeworklist)
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
            val intent = Intent(this, Activity_Settings::class.java)
            startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment =
            when (position) {
                0 -> Fragment_Examlist()
                1 -> Fragment_Timetable()
                2 -> Fragment_Homeworklist()
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