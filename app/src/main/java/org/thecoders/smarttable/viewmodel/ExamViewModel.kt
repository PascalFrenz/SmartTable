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

    /**
     * Contains a reference to the data in the DB
     */
    val examList: LiveData<List<Exam>>

    init {
        //Get the AppDatabase via Dagger 2
        (application as SmartTableApplication).appComponent.inject(this)

        //Loads data into the ViewModel
        examList = db.examModel().loadExams()
    }

    /**
     * Adds an Exam-Object to the database
     */
    fun addExam(exam: Exam) =
            AsyncTask.execute {
                db.examModel().insert(exam)
            }

    /**
     * Deletes a specified Exam object from db if present
     */
    fun deleteExam(exam: Exam) =
            AsyncTask.execute {
                db.examModel().delete(exam)
            }

    /**
     * Updates an existing Exam-Object
     */
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