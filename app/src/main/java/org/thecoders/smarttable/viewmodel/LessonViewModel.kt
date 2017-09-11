package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask
import android.util.Log
import org.thecoders.smarttable.SmartTableApplication
import org.thecoders.smarttable.data.*
import org.thecoders.smarttable.ui.Adapter_Lesson
import javax.inject.Inject

/**
 * Created by frenz on 13.07.2017.
 */
class LessonViewModel(application: Application) : AndroidViewModel(application) {

    private val LOG_TAG = LessonViewModel::class.java.simpleName

    @Inject lateinit var db: AppDatabase

    init {
        (application as SmartTableApplication).appComponent.inject(this)
    }

    fun loadMondayLessons() = db.timetableModel().loadMondayLessons()
    fun loadTuesdayLessons() = db.timetableModel().loadTuesdayLessons()
    fun loadWednesdayLessons() = db.timetableModel().loadWednesdayLessons()
    fun loadThursdayLessons() = db.timetableModel().loadThursdayLessons()
    fun loadFridayLessons() = db.timetableModel().loadFridayLessons()

    private fun updateMondayLesson(lesson: LessonMon) = db.timetableModel().updateMondayLesson(id = lesson.id, mon_timing = lesson.mon_timing, mon = lesson.mon)
    private fun updateTuesdayLesson(lesson: LessonTue) = db.timetableModel().updateTuesdayLesson(id = lesson.id, tue_timing = lesson.tue_timing, tue = lesson.tue)
    private fun updateWednesdayLesson(lesson: LessonWed) = db.timetableModel().updateWednesdayLesson(id = lesson.id, wed_timing = lesson.wed_timing, wed = lesson.wed)
    private fun updateThursdayLesson(lesson: LessonThu) = db.timetableModel().updateThursdayLesson(id = lesson.id, thu_timing = lesson.thu_timing, thu = lesson.thu)
    private fun updateFridayLesson(lesson: LessonFri) = db.timetableModel().updateFridayLesson(id = lesson.id, fri_timing = lesson.fri_timing, fri = lesson.fri)

    fun insert(row: TimetableRow) = AsyncTask.execute { db.timetableModel().insert(row) }

    fun insertOrUpdateLesson(lesson: Lesson, day: String) {
        val lessonCount = loadMondayLessons().lastIndex + 1
        Log.v(LOG_TAG, "LessonCount: $lessonCount\nLessonID: ${lesson.id}" )

        when (day) {
            "monday" -> {
                val lessonMon = LessonMon(lesson.id, lesson.timing, lesson.subjectName)
                if (lesson.id <= lessonCount) updateMondayLesson(lessonMon)
                else insert(TimetableRow(id = lessonMon.id, mon_timing = lessonMon.mon_timing, mon = lessonMon.mon))
            }

            "tuesday" -> {
                lesson as LessonTue
                if(lesson.id <= lessonCount) updateTuesdayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, tue_timing = lesson.tue_timing, tue = lesson.tue))
            }

            "wednesday" -> {
                lesson as LessonWed
                if(lesson.id <= lessonCount) updateWednesdayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, wed_timing = lesson.wed_timing, wed = lesson.wed))
            }

            "thursday" -> {
                lesson as LessonThu
                if(lesson.id <= lessonCount) updateThursdayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, thu_timing = lesson.thu_timing, thu = lesson.thu))
            }

            "friday" -> {
                lesson as LessonFri
                if(lesson.id <= lessonCount) updateFridayLesson(lesson)
                else insert(TimetableRow(id = lesson.id, fri_timing = lesson.fri_timing, fri = lesson.fri))
            }
        }
    }

    fun delete(row: TimetableRow) =
            AsyncTask.execute {
                db.timetableModel().delete(row)
            }

    companion object {
        class LoadLessonsIntoAdapter(
                val adapter: Adapter_Lesson,
                private val day: String,
                private val lessonViewModel: LessonViewModel
        ) : AsyncTask<String, Int, List<Lesson>>() {

            override fun doInBackground(vararg params: String?): List<Lesson> {
                return when (day) {
                    "monday" -> lessonViewModel.loadMondayLessons()
                    "tuesday" -> lessonViewModel.loadTuesdayLessons()
                    "wednesday" -> lessonViewModel.loadWednesdayLessons()
                    "thursday" -> lessonViewModel.loadThursdayLessons()
                    "friday" -> lessonViewModel.loadFridayLessons()
                    else -> mutableListOf()
                }
            }

            override fun onPostExecute(result: List<Lesson>) {
                if(result.isNotEmpty()) {
                    adapter.clear()
                    result.forEach(adapter::add)
                }
            }
        }

        class LoadLessonsByDay(
                private val day: String,
                private val lessonViewModel: LessonViewModel
        ) : AsyncTask<String, Int, List<Lesson>>() {
            override fun doInBackground(vararg params: String?): List<Lesson> {
                return when (day) {
                    "monday" -> lessonViewModel.loadMondayLessons().filter { it.mon != "NULL" }
                    "tuesday" -> lessonViewModel.loadTuesdayLessons()
                    "wednesday" -> lessonViewModel.loadWednesdayLessons()
                    "thursday" -> lessonViewModel.loadThursdayLessons()
                    "friday" -> lessonViewModel.loadFridayLessons()
                    else -> mutableListOf()
                }
            }
        }
    }
}