package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask
import org.thecoders.smarttable.SmartTableApplication
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.Exam
import javax.inject.Inject

/**
 * Created by frenz on 24.06.2017.
 */
class ExamViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var db: AppDatabase

    init {
        (application as SmartTableApplication).appComponent.inject(this)
    }

    fun loadExamList() = db.examModel().loadExams()

    fun addExam(exam: Exam) =
            AsyncTask.execute {
                db.examModel().insertOrUpdate(exam)
            }

    fun deleteExam(exam: Exam) =
            AsyncTask.execute {
                db.examModel().delete(exam)
            }
}