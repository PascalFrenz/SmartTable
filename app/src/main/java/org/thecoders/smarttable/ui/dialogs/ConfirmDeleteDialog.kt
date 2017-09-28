package org.thecoders.smarttable.ui.dialogs

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.Toast
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.AppDatabase
import org.thecoders.smarttable.data.pojos.*
import org.thecoders.smarttable.ui.adapters.ExamAdapter
import org.thecoders.smarttable.ui.adapters.HomeworkAdapter
import org.thecoders.smarttable.ui.adapters.LessonAdapter
import org.thecoders.smarttable.ui.adapters.SubjectAdapter


/**
 * Created by Pascal on 03.09.2016.
 * This is the dialog interface which is shown to the user when he attempts to delete an entry
 * from the database. It makes sure the user did not toggle the action accidentally
 */
//Empty Constructor as required by Guidelines
class ConfirmDeleteDialog : DialogFragment() {

    var objectToDelete: Any? = null
    var objectAdapter: Any? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val db = AppDatabase.getPersistenceDatabase(context)

        when (objectToDelete) {
            is Homework -> {
                val homeworkToDelete = objectToDelete as Homework
                val homeworkAdapter = objectAdapter as HomeworkAdapter
                builder.setMessage(R.string.title_confirm_delete_dialog)
                        .setPositiveButton(R.string.action_delete) { dialog, _ ->
                            AsyncTask.execute {
                                db.homeworkModel().delete(homeworkToDelete)
                            }
                            homeworkAdapter.data.remove(homeworkToDelete)
                            homeworkAdapter.notifyItemRemoved(homeworkAdapter.data.indexOf(homeworkToDelete))
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }

            }
            is Exam -> {
                val examToDelete = objectToDelete as Exam
                val examAdapter = objectAdapter as ExamAdapter
                builder.setMessage(R.string.title_confirm_delete_dialog)
                        .setPositiveButton(R.string.action_delete) { dialog, _ ->
                            AsyncTask.execute {
                                db.examModel().delete(examToDelete)
                            }
                            examAdapter.data.remove(examToDelete)
                            examAdapter.notifyItemRemoved(examAdapter.data.indexOf(examToDelete))
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
            }
            is Subject -> {
                val subjectToDelete = objectToDelete as Subject
                val subjectAdapter = objectAdapter as SubjectAdapter
                builder.setMessage(R.string.title_confirm_delete_dialog)
                        .setPositiveButton(R.string.action_delete) { dialog, _ ->
                            AsyncTask.execute {
                                db.subjectModel().delete(subjectToDelete)
                            }
                            subjectAdapter.data.remove(subjectToDelete)
                            subjectAdapter.notifyItemRemoved(subjectAdapter.data.indexOf(subjectToDelete))
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
            }
            is Lesson -> {
                val lessonToDelete = objectToDelete as Lesson
                val lessonAdapter = objectAdapter as LessonAdapter
                builder.setMessage(R.string.title_confirm_delete_dialog)
                        .setPositiveButton(R.string.action_delete) { dialog, _ ->
                            AsyncTask.execute {
                                if(lessonToDelete is LessonMon)
                                    db.timetableModel().updateMondayLesson(lessonToDelete.id, "NULL", "NULL")
                                else Toast.makeText(context, "Something went wrong!", Toast.LENGTH_LONG).show()
                                //TODO: Extend this until every day is covered!
                            }
                            lessonAdapter.data.remove(lessonToDelete)
                            lessonAdapter.notifyItemRemoved(lessonToDelete.id.toInt())
                            dialog.dismiss()
                        }
                        .setNegativeButton(R.string.action_cancel) { dialog, _ ->
                            dialog.dismiss()
                        }
            }
        }
        return builder.create()
    }
}
