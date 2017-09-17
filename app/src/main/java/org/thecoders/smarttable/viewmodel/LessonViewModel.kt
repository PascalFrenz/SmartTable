package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import android.util.Log
import org.thecoders.smarttable.SmartTableApplication
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.pojos.*
import javax.inject.Inject

/**
 * Created by frenz on 13.07.2017.
 */
class LessonViewModel(application: Application) : AndroidViewModel(application) {


    @Inject lateinit var db: AppDatabase
    val mondayLessons: LiveData<List<LessonMon>>
    val tuesdayLessons: LiveData<List<LessonTue>>
    val wednesdayLessons: LiveData<List<LessonWed>>
    val thursdayLessons: LiveData<List<LessonThu>>
    val fridayLessons: LiveData<List<LessonFri>>

    init {
        (application as SmartTableApplication).appComponent.inject(this)
        mondayLessons = db.timetableModel().loadMondayLessons()
        tuesdayLessons = db.timetableModel().loadTuesdayLessons()
        wednesdayLessons = db.timetableModel().loadWednesdayLessons()
        thursdayLessons = db.timetableModel().loadThursdayLessons()
        fridayLessons = db.timetableModel().loadFridayLessons()
    }

    private fun updateMondayLesson(lesson: LessonMon) = db.timetableModel().updateMondayLesson(id = lesson.id, mon_timing = lesson.mon_timing, mon = lesson.mon)
    private fun updateTuesdayLesson(lesson: LessonTue) = db.timetableModel().updateTuesdayLesson(id = lesson.id, tue_timing = lesson.tue_timing, tue = lesson.tue)
    private fun updateWednesdayLesson(lesson: LessonWed) = db.timetableModel().updateWednesdayLesson(id = lesson.id, wed_timing = lesson.wed_timing, wed = lesson.wed)
    private fun updateThursdayLesson(lesson: LessonThu) = db.timetableModel().updateThursdayLesson(id = lesson.id, thu_timing = lesson.thu_timing, thu = lesson.thu)
    private fun updateFridayLesson(lesson: LessonFri) = db.timetableModel().updateFridayLesson(id = lesson.id, fri_timing = lesson.fri_timing, fri = lesson.fri)

    fun insert(row: TimetableRow) = AsyncTask.execute { db.timetableModel().insert(row) }

    fun insertOrUpdateLesson(lesson: Lesson, day: String) {
        val lastLessonId: Int =
                if(mondayLessons.value?.lastIndex == -1) 0 //TODO: Rework Null-Handling
                else mondayLessons.value!!.lastIndex + 1
        Log.v(LOG_TAG, "LastLessonId: $lastLessonId\n" + lesson.toString() )

        when (day) {
            "monday" -> {
                val lessonMon = LessonMon(lesson.id, lesson.timing, lesson.subjectName)
                if (lesson.id <= lastLessonId) updateMondayLesson(lessonMon)
                else insert(TimetableRow(id = lessonMon.id, mon_timing = lessonMon.mon_timing, mon = lessonMon.mon))
            }

            "tuesday" -> {
                lesson as LessonTue
                if(lesson.id <= lastLessonId) updateTuesdayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, tue_timing = lesson.tue_timing, tue = lesson.tue))
            }

            "wednesday" -> {
                lesson as LessonWed
                if(lesson.id <= lastLessonId) updateWednesdayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, wed_timing = lesson.wed_timing, wed = lesson.wed))
            }

            "thursday" -> {
                lesson as LessonThu
                if(lesson.id <= lastLessonId) updateThursdayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, thu_timing = lesson.thu_timing, thu = lesson.thu))
            }

            "friday" -> {
                lesson as LessonFri
                if(lesson.id <= lastLessonId) updateFridayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, fri_timing = lesson.fri_timing, fri = lesson.fri))
            }
        }
    }

    fun delete(row: TimetableRow) =
            AsyncTask.execute {
                db.timetableModel().delete(row)
            }

    companion object {
        private val LOG_TAG = LessonViewModel::class.java.simpleName
    }
}