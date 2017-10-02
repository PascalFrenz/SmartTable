package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import org.thecoders.smarttable.SmartTableApplication
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.pojos.Homework
import javax.inject.Inject

/**
 * Created by frenz on 12.06.2017.
 */

class HomeworkViewModel constructor(application: Application) : AndroidViewModel(application) {

    @Inject lateinit var db: AppDatabase
    val homeworkList: LiveData<List<Homework>>

    init {
        (application as SmartTableApplication).appComponent.inject(this)
        homeworkList = db.homeworkModel().loadHomework()
    }

    fun addHomework(homework: Homework) =
            AsyncTask.execute({
                db.homeworkModel().insert(homework)
            })

    fun deleteHomework(homework: Homework) =
            AsyncTask.execute({
                db.homeworkModel().delete(homework)
            })

    fun updateHomework(homework: Homework) =
            AsyncTask.execute({
                db.homeworkModel().updateById(
                        homework.id,
                        homework.subject,
                        homework.task,
                        homework.date_start,
                        homework.date_deadline,
                        homework.finished,
                        homework.effort
                )
            })
}

