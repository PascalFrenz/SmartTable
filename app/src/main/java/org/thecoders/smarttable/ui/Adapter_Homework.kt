package org.thecoders.smarttable.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.listview_item_homework.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.Homework
import java.util.*

/**
 * Created by Pascal on 31.03.2016.

 * This is the Adapter for Homeworklists, where Homeworkitems are displayed.
 * It sets up the view and fills it with the Information given to it by
 * the DataArray of the List.

 * The items can be deleted via a delete icon which toggles a dialog, where
 * the user is asked to acknowledge the deletion.
 * When acknowledged, the selected Homework is deleted in the List, as well
 * as in the Database. Look at [Dialog_ConfirmDelete] to learn more
 * about the deletion Dialog and at the implementation of the Homework object
 * to learn more about how it is deleted in the DB

 * Wether the option of deletion should be available or not is set in the
 * constructor.

 */
class Adapter_Homework(context: Context, val layoutRecourceId: Int,
                       var data: MutableList<Homework>, val enableEdit: Boolean) :
        ArrayAdapter<Homework>(context, layoutRecourceId, data) {

    private class HomeworkHolder {
        lateinit var mSubject: TextView
        lateinit var mTask: TextView
        lateinit var mFinishDate: TextView
        lateinit var mDeleteIcon: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        val holder: HomeworkHolder

        if (row == null) {
            val inflater = (context as AppCompatActivity).layoutInflater

            row = inflater.inflate(layoutRecourceId, parent, false)
            holder = HomeworkHolder()

            initHolder(holder, row)
            row.tag = holder

        } else {
            holder = row.tag as HomeworkHolder
        }

        val homework = data[position]

        setupHolder(holder, homework, row!!)

        return row
    }


    private fun displayDeleteDialog(homework: Homework, context: Context) {
        val confirmDeleteDialog = Dialog_ConfirmDelete()
        confirmDeleteDialog.objectToDelete = homework
        confirmDeleteDialog.objectAdapter = this
        confirmDeleteDialog.show((context as AppCompatActivity).supportFragmentManager, LOG_TAG)
    }


    private fun initHolder(holder: HomeworkHolder, row: View) {
        holder.mSubject = row.item_homework_subject
        holder.mTask = row.item_homework_task
        holder.mFinishDate = row.item_homework_deadline
        holder.mDeleteIcon = row.item_homework_delete
    }

    private fun setupHolder(holder: HomeworkHolder, homework: Homework, row: View) {
        val today = DateConverter.dateFormat.format(Date())
        val deadline = DateConverter.dateFormat.format(homework.date_deadline)

        val timeToDeadline = DateConverter().getDifference(today, deadline)

        when {
            homework.finished -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.done))
            timeToDeadline <= 1 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority4))
            timeToDeadline <= 3 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority3))
            timeToDeadline <= 7 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority2))
            timeToDeadline <= 14 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority1))
            else -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority0))
        }

        Log.v(LOG_TAG, homework.toString())

        holder.mSubject.text = "${homework.subject}: "
        holder.mTask.text = "${homework.task} (${homework.effort})"
        holder.mFinishDate.text = "$timeToDeadline days left ($deadline)"

        if (enableEdit) {
            holder.mDeleteIcon.setOnClickListener { displayDeleteDialog(homework, context) }
        } else {
            holder.mDeleteIcon.isClickable = false
            holder.mDeleteIcon.visibility = View.INVISIBLE
        }
    }

    companion object {
        private val LOG_TAG = Adapter_Homework::class.java.simpleName
    }

}
