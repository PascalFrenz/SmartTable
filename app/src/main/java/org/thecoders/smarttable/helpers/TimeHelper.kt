package org.thecoders.smarttable.helpers

import android.content.Context
import android.preference.PreferenceManager
import org.thecoders.smarttable.ui.fragments.SettingsFragment

/**
 * Created by frenz on 31.07.2017.
 */

class TimeHelper {

    companion object {
        private fun getStartAndEndTime(timing: String): List<String>
                = timing.replace(" ", "").split("-")

        fun getHours(time: String): String {
            val pieces = time.split(":")
            val hour = pieces[0].toInt() + (pieces[1].toInt() / 60)

            return hour.toString()
        }

        fun getMinutes(time: String): String {
            val pieces = time.split(":")
            val minutes = (pieces[0].toInt() * 60) + pieces[1].toInt()

            return minutes.toString()
        }

        fun addTime(time: String, minutes: String) : String {
            val minutesToAdd = minutes.toInt()

            val time_hours = time.split(":")[0].toInt()
            val time_minutes = time.split(":")[1].toInt() + minutesToAdd
            val newTime = mutableListOf(time_hours, time_minutes)

            while(newTime[1] >= 60) {
                newTime[0] += 1
                newTime[1] -= 60
            }

            val sb = StringBuilder()

            if(newTime[0] < 10) sb.append("0")
            sb.append(newTime[0])
            sb.append(":")
            if(newTime[1] < 10) sb.append("0")
            sb.append(newTime[1])

            return sb.toString()

        }

        fun buildTiming(startAndEndTimes: List<String>) : String
                = StringBuilder()
                .append(startAndEndTimes[0])
                .append(" - ")
                .append(startAndEndTimes[1])
                .toString()

        fun calcTimingById(context: Context, id: Int) : String {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val startOfDay = preferences.getString(SettingsFragment.ID_DAY_START, "")
            val lessonDuration = getMinutes(preferences.getString(SettingsFragment.ID_LESSON_LENGTH, ""))

            val startTime = addTime(startOfDay, (lessonDuration.toInt() * id).toString())
            val endTime = addTime(startTime, lessonDuration)

            return buildTiming(listOf(startTime, endTime))
        }


        fun addTimeToStart(timing: String, minutes: String) : String {
            val startTime = getStartAndEndTime(timing)[0]
            val endTime = getStartAndEndTime(timing)[1]

            return buildTiming(listOf(addTime(startTime, minutes), endTime))
        }

        fun addTimeToEnd(timing: String, minutes: String) : String {
            val startTime = getStartAndEndTime(timing)[0]
            val endTime = getStartAndEndTime(timing)[1]

            return buildTiming(listOf(startTime, addTime(endTime, minutes)))
        }

        fun addTimeToTiming(timing: String, minutes: String) : String {
            val startTime = getStartAndEndTime(timing)[0]
            val endTime = getStartAndEndTime(timing)[1]

            return buildTiming(listOf(addTime(startTime, minutes), addTime(endTime, minutes)))
        }
    }


}