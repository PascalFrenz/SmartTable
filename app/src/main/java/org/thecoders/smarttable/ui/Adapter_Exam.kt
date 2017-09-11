package org.thecoders.smarttable.ui

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.listview_item_exam.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.Exam
import java.util.*

/**
 * Created by frenz on 24.06.2017.
 */

class Adapter_Exam(context: Context, val layoutResourceId: Int,
                   val data: MutableList<Exam>, val enableEdit: Boolean) :
        ArrayAdapter<Exam>(context, layoutResourceId, data) {

    class ExamHolder {
        lateinit var mSubject: TextView
        lateinit var mTopic: TextView
        lateinit var mDate: TextView
        lateinit var mDeleteIcon: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var row = convertView
        val holder: ExamHolder

        if(row == null) {
            val inflater = (context as AppCompatActivity).layoutInflater

            row = inflater.inflate(layoutResourceId, parent, false)!!
            holder = ExamHolder()

            initHolder(holder, row)
            row.tag = holder

        } else {
            holder = row.tag as ExamHolder
        }

        val exam = data[position]

        setupHolder(holder, exam, row)

        return row
    }

    private fun initHolder(holder: ExamHolder, row: View) {
        holder.mSubject = row.item_exam_subject
        holder.mTopic = row.item_exam_topic
        holder.mDate = row.item_exam_date
        holder.mDeleteIcon = row.item_exam_delete
    }

    private fun setupHolder(holder: ExamHolder, exam: Exam, row: View) {
        val today = DateConverter.dateFormat.format(Date())
        val examDate = DateConverter.dateFormat.format(exam.date)

        val timeToExam = DateConverter().getDifference(today, examDate)

        when {
            timeToExam <= 1 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority4))
            timeToExam <= 3 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority3))
            timeToExam <= 7 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority2))
            timeToExam <= 14 -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority1))
            else -> row.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority0))
        }

        holder.mSubject.text = "${exam.subject}: "
        holder.mTopic.text = exam.topic
        holder.mDate.text = "$timeToExam days left ($examDate)"

        if (enableEdit) {
            holder.mDeleteIcon.setOnClickListener { displayDeleteDialog(exam, context) }
        } else {
            holder.mDeleteIcon.isClickable = false
            holder.mDeleteIcon.visibility = View.INVISIBLE
        }
    }

    private fun displayDeleteDialog(exam: Exam, context: Context) {
        val confirmDeleteDialog = Dialog_ConfirmDelete()
        confirmDeleteDialog.objectToDelete = exam
        confirmDeleteDialog.objectAdapter = this
        confirmDeleteDialog.show((context as AppCompatActivity).supportFragmentManager, LOG_TAG)
    }

    companion object {
        private val LOG_TAG = Adapter_Exam::class.java.simpleName
    }

}