package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.os.AsyncTask
import org.thecoders.smarttable.SmartTableApplication
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.Subject
import javax.inject.Inject

/**
 * Created by frenz on 24.06.2017.
 */

class SubjectViewModel(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var db: AppDatabase

    init {
        (application as SmartTableApplication).appComponent.inject(this)
    }

    fun loadSubjectList() = db.subjectModel().loadSubjects()

    fun loadSubjectNames() = db.subjectModel().loadSubjectNames()

    fun addSubject(subject: Subject) =
            AsyncTask.execute {
                db.subjectModel().insertOrUpdate(subject)
            }

    fun deleteSubject(subject: Subject) =
            AsyncTask.execute {
                db.subjectModel().delete(subject)
            }
}