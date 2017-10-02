package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import org.thecoders.smarttable.SmartTableApplication
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.pojos.Exam
import javax.inject.Inject

/**
 * Created by frenz on 24.06.2017.
 */
class ExamViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var db: AppDatabase
    val examList: LiveData<List<Exam>>

    init {
        (application as SmartTableApplication).appComponent.inject(this)
        examList = db.examModel().loadExams()
    }

    fun addExam(exam: Exam) =
            AsyncTask.execute {
                db.examModel().insert(exam)
            }

    fun deleteExam(exam: Exam) =
            AsyncTask.execute {
                db.examModel().delete(exam)
            }

    fun updateExam(exam: Exam) =
            AsyncTask.execute{
                db.examModel().updateById(
                        exam.id,
                        exam.subject,
                        exam.topic,
                        exam.date,
                        exam.grade
                )
            }
}