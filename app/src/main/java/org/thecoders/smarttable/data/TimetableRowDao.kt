package org.thecoders.smarttable.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by frenz on 13.07.2017.
 */

@Dao
interface TimetableRowDao {
    @Query("select id, mon_timing, mon from timetable")
    fun loadMondayLessons(): List<LessonMon>

    @Query("select id, tue_timing, tue from timetable")
    fun loadTuesdayLessons(): List<LessonTue>

    @Query("select id, wed_timing, wed from timetable")
    fun loadWednesdayLessons(): List<LessonWed>

    @Query("select id, thu_timing, thu from timetable")
    fun loadThursdayLessons(): List<LessonThu>

    @Query("select id, fri_timing, fri from timetable")
    fun loadFridayLessons(): List<LessonFri>

    @Query("select * from timetable where id = :id")
    fun loadLessonRowById(id: Long): TimetableRow

    @Query("select * from timetable")
    fun loadTimetable(): List<TimetableRow>

    @Query("update timetable set mon_timing = :mon_timing, mon = :mon where id = :id")
    fun updateMondayLesson(id: Long, mon_timing: String, mon: String)

    @Query("update timetable set tue_timing = :tue_timing, tue = :tue where id = :id")
    fun updateTuesdayLesson(id: Long, tue_timing: String, tue: String)

    @Query("update timetable set wed_timing = :wed_timing, wed = :wed where id = :id")
    fun updateWednesdayLesson(id: Long, wed_timing: String, wed: String)

    @Query("update timetable set thu_timing = :thu_timing, thu = :thu where id = :id")
    fun updateThursdayLesson(id: Long, thu_timing: String, thu: String)

    @Query("update timetable set fri_timing = :fri_timing, fri = :fri where id = :id")
    fun updateFridayLesson(id: Long, fri_timing: String, fri: String)

    @Insert
    fun insert(row: TimetableRow)

    @Delete
    fun delete(row: TimetableRow)
}