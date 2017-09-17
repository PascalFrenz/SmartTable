package org.thecoders.smarttable.ui

import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_modify_day.*
import org.thecoders.smarttable.R

class Activity_ModifyDay : AppCompatActivity() {

    interface SaveDayListener {
        fun saveDay()
    }

    private lateinit var mCallback: SaveDayListener


    private lateinit var mModifyDayFragment: Fragment_ModifyDay
    private lateinit var mDay: String


    companion object {
        private val LOG_TAG = Activity_ModifyDay::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_day)
        mDay = intent.extras.getString("day")


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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_modify_day, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_save) {
            mCallback.saveDay()
            NavUtils.navigateUpFromSameTask(this)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
