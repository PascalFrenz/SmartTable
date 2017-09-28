package org.thecoders.smarttable.ui.fragments

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import org.thecoders.smarttable.R

/**
 * Created by frenz on 12.07.2017.
 */

class SettingsFragment : PreferenceFragment() {

    companion object {
        private val LOG_TAG = SettingsFragment::class.java.simpleName

        val ID_LESSON_LENGTH = "pref_lesson_length"
        val ID_DAY_START = "pref_day_start"

        lateinit var PREF_LESSON_LENGTH: Preference
        lateinit var PREF_DAY_STARTTIME: Preference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_general)

        init()
    }

    private fun init() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)

        PREF_LESSON_LENGTH = findPreference(ID_LESSON_LENGTH)
        PREF_LESSON_LENGTH.summary = sharedPreferences.getString(ID_LESSON_LENGTH, "")
        PREF_LESSON_LENGTH.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newVal ->
            val pref_lesson_length = newVal as String
            PREF_LESSON_LENGTH.summary = pref_lesson_length
            true
        }

        PREF_DAY_STARTTIME = findPreference(ID_DAY_START)
        PREF_DAY_STARTTIME.summary = sharedPreferences.getString(ID_DAY_START, "")
        PREF_DAY_STARTTIME.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newVal ->
            val pref_day_start = newVal as String
            PREF_DAY_STARTTIME.summary = pref_day_start
            true
        }
    }

}