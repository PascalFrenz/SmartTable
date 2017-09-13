package org.thecoders.smarttable.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_modify_day.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.Lesson
import org.thecoders.smarttable.viewmodel.LessonViewModel

class Activity_ModifyDay : AppCompatActivity() {

    interface SaveDayListener {
        fun getLessonsToSave(): MutableList<Lesson>
    }

    lateinit var mLessonViewModel: LessonViewModel
    private lateinit var mModifyDayFragment: Fragment_ModifyDay
    lateinit var mDay: String
    private lateinit var mCallback: SaveDayListener

    companion object {
        private val LOG_TAG = Activity_ModifyDay::class.java.simpleName
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_day)
        mDay = intent.extras.getString("day")

        mLessonViewModel = ViewModelProviders.of(this).get(LessonViewModel::class.java)

        if(activity_modifyday_content != null) {
            if(savedInstanceState != null)
                return

            mModifyDayFragment = Fragment_ModifyDay()
            mModifyDayFragment.arguments = intent.extras

            mCallback = try {
                mModifyDayFragment
            } catch (e: ClassCastException) {
                throw ClassCastException(mModifyDayFragment.toString() + "must implement OnAddHomeworkPressedListener")
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_modifyday_content, mModifyDayFragment)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_modify_day, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId

        if (id == R.id.action_save) {

            val data: MutableList<Lesson> = mCallback.getLessonsToSave()

            AsyncTask.execute {
                val tableCount = mLessonViewModel.loadMondayLessons().size
                var dataLength = data.size

                while (dataLength < tableCount) {
                    dataLength++
                    data.add(Lesson(dataLength.toLong()))
                }

                data.forEach {
                    mLessonViewModel.insertOrUpdateLesson(it, mDay)
                    Log.v(LOG_TAG, it.toString())
                }
            }

            NavUtils.navigateUpFromSameTask(this)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
