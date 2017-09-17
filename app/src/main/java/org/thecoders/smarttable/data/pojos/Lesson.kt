package org.thecoders.smarttable.data.pojos

import android.arch.persistence.room.Ignore

/**
 * Created by frenz on 13.07.2017.
 */

open class Lesson(
        var id: Long,
        @Ignore var timing: String = "NULL",
        @Ignore val subjectName: String = "NULL"
) {
    override fun toString(): String {
        return "ID: $id\n" +
                "Timing: $timing\n" +
                "Subject: $subjectName"
    }
}

class LessonMon(id: Long, val mon_timing: String, val mon: String) : Lesson(id, mon_timing, mon)
class LessonTue(id: Long, val tue_timing: String, val tue: String) : Lesson(id, tue_timing, tue)
class LessonWed(id: Long, val wed_timing: String, val wed: String) : Lesson(id, wed_timing, wed)
class LessonThu(id: Long, val thu_timing: String, val thu: String) : Lesson(id, thu_timing, thu)
class LessonFri(id: Long, val fri_timing: String, val fri: String) : Lesson(id, fri_timing, fri)

