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

    fun insertOrUpdateLesson(lesson: Lesson, day: String, lastLessonIndex: Int) {
        Log.v(LOG_TAG, "LastLessonId: $lastLessonIndex\n" + lesson.toString() )

        when (day) {
            "monday" -> {
                val lessonMon = LessonMon(lesson.id, lesson.timing, lesson.subjectName)
                if (lesson.id <= lastLessonIndex) updateMondayLesson(lessonMon)
                else insert(TimetableRow(id = lessonMon.id, mon_timing = lessonMon.mon_timing, mon = lessonMon.mon))
            }

            "tuesday" -> {
                val lessonTue = LessonTue(lesson.id, lesson.timing, lesson.subjectName)
                if(lesson.id <= lastLessonIndex) updateTuesdayLesson(lessonTue)
                else insert(TimetableRow(id = lessonTue.id, tue_timing = lessonTue.tue_timing, tue = lessonTue.tue))
            }

            "wednesday" -> {
                val lessonWed = LessonWed(lesson.id, lesson.timing, lesson.subjectName)
                if(lesson.id <= lastLessonIndex) updateWednesdayLesson(lessonWed)
                else insert(TimetableRow(id = lessonWed.id, wed_timing = lessonWed.wed_timing, wed = lessonWed.wed))
            }

            "thursday" -> {
                val lessonThu = LessonThu(lesson.id, lesson.timing, lesson.subjectName)
                if(lesson.id <= lastLessonIndex) updateThursdayLesson(lessonThu)
                else insert(TimetableRow(id = lessonThu.id, thu_timing = lessonThu.thu_timing, thu = lessonThu.thu))
            }

            "friday" -> {
                val lessonFri = LessonFri(lesson.id, lesson.timing, lesson.subjectName)
                if(lesson.id <= lastLessonIndex) updateFridayLesson(lessonFri)
                else insert(TimetableRow(id = lessonFri.id, fri_timing = lessonFri.fri_timing, fri = lessonFri.fri))
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