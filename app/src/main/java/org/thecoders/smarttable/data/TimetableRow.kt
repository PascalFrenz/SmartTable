package org.thecoders.smarttable.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by frenz on 13.07.2017.
 */

@Entity(tableName = "timetable")
class TimetableRow(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val mon_timing: String = "NULL",
        val mon: String = "NULL",
        val tue_timing: String = "NULL",
        val tue: String = "NULL",
        val wed_timing: String = "NULL",
        val wed: String = "NULL",
        val thu_timing: String = "NULL",
        val thu: String = "NULL",
        val fri_timing: String = "NULL",
        val fri: String = "NULL") {

    @Ignore
    constructor(mon: LessonMon, tue: LessonTue, wed: LessonWed, thu: LessonThu, fri: LessonFri) :
            this(
                0,
                mon.mon_timing,
                mon.mon,
                tue.tue_timing,
                tue.tue,
                wed.wed_timing,
                wed.wed,
                thu.thu_timing,
                thu.thu,
                fri.fri_timing,
                fri.fri
            )
}