package org.thecoders.smarttable.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
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

    init {
        (application as SmartTableApplication).appComponent.inject(this)
    }

    fun loadHomeworkList() = db.homeworkModel().loadHomework()

    fun addHomework(homework: Homework) =
            AsyncTask.execute({
                db.homeworkModel().insertOrUpdate(homework)
            })

    fun deleteHomework(homework: Homework) =
            AsyncTask.execute({
                db.homeworkModel().delete(homework)
            })
}

