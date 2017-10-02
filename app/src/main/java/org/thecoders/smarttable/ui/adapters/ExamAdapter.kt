package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Exam
import java.util.*

/**
 * Created by frenz on 24.06.2017.
 */

class ExamAdapter(val context: Context, var data: MutableList<Exam>, val enableEdit: Boolean,
                  val callback: OnExamEditClickListener) :
        RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    interface OnExamEditClickListener {
        fun onExamEditRequest(exam: Exam)
    }

    companion object {
        private val LOG_TAG = ExamAdapter::class.java.simpleName
    }

    inner class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            //Init the views inside the itemView
            val mSubject: TextView = itemView.findViewById(R.id.item_exam_subject)
            val mTopic: TextView = itemView.findViewById(R.id.item_exam_topic)
            val mDate: TextView = itemView.findViewById(R.id.item_exam_date)
            val mEditIcon: ImageView = itemView.findViewById(R.id.item_exam_edit)

            //Fetch the item from the data
            val exam = data[position]

            //Setup all the fields according to the items data
            val today = DateConverter.dateFormat.format(Date())
            val examDate = DateConverter.dateFormat.format(exam.date)

            val timeToExam = DateConverter().getDifference(today, examDate)

            when {
                timeToExam <= 1 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority4))
                timeToExam <= 3 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority3))
                timeToExam <= 7 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority2))
                timeToExam <= 14 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority1))
                else -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.test_priority0))
            }

            mSubject.text = "${exam.subject}: "
            mTopic.text = exam.topic
            mDate.text = "$timeToExam days left ($examDate)"

            if (enableEdit) {
                mEditIcon.setOnClickListener { callback.onExamEditRequest(exam) }
            } else {
                mEditIcon.isClickable = false
                mEditIcon.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.listview_item_exam,
                parent,
                false
        )

        return ExamViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = data.count()

    fun alterItems(newList: List<Exam>) {
        data = newList.toMutableList()
        notifyDataSetChanged()
    }
}