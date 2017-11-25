package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.helpers.AbstractAdapterInterface
import org.thecoders.smarttable.helpers.TimeHelper
import java.lang.ref.WeakReference

/**
 * Created by frenz on 24.06.2017.
 */

class ExamAdapter(weakContext: WeakReference<Context>, var data: MutableList<Exam>, val enableEdit: Boolean,
                  val callback: AbstractAdapterInterface) :
        RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    val context: Context? by lazy(weakContext::get)

    var multiSelect: Boolean = false
    var selectedItems: MutableList<Exam> = mutableListOf()

    private val actionModeCallbacks: ActionMode.Callback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            multiSelect = true
            menu.add("Delete")
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean = false

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            //TODO: Noch nicht optimal -> Für jedes Element wird der ConfirmDeleteDialog aufgerufen.
            selectedItems.forEach {
                this@ExamAdapter.callback.onObjectDeleteRequest(it, this@ExamAdapter)
            }

            mode.finish()
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            multiSelect = false
            selectedItems.clear()
            notifyDataSetChanged()
        }
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
            val timeToExam = TimeHelper.calcTimeToDeadline(exam.date)
            val examDate = DateConverter.dateFormat.format(exam.date)

            if (selectedItems.contains(exam)) {
                itemView.setBackgroundColor(Color.LTGRAY)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    itemView.elevation = 12f
            } else {
                itemView.setBackgroundColor(getExamColor(exam))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    itemView.elevation = 0f
            }

            mSubject.text = "${exam.subject}: "
            mTopic.text = exam.topic
            mDate.text = "$timeToExam days left ($examDate)"

            if (enableEdit) {
                mEditIcon.setOnClickListener { callback.onObjectEditRequest(exam) }
            } else {
                mEditIcon.isClickable = false
                mEditIcon.visibility = View.INVISIBLE
            }

            itemView.setOnLongClickListener {
                //callback.onExamDeleteRequest(exam, this@ExamAdapter)
                (context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
                selectItem(exam)
                return@setOnLongClickListener true
            }

            itemView.setOnClickListener {
                selectItem(exam)
            }
        }

        private fun getExamColor(exam: Exam): Int {
            val timeToExam = TimeHelper.calcTimeToDeadline(exam.date)

            return when {
                timeToExam <= 1 -> ContextCompat.getColor(context, R.color.test_priority4)
                timeToExam <= 3 -> ContextCompat.getColor(context, R.color.test_priority3)
                timeToExam <= 7 -> ContextCompat.getColor(context, R.color.test_priority2)
                timeToExam <= 14 -> ContextCompat.getColor(context, R.color.test_priority1)
                else -> ContextCompat.getColor(context, R.color.test_priority0)
            }
        }

        private fun selectItem(item: Exam) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item)
                    itemView.setBackgroundColor(getExamColor(item))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        itemView.elevation = 0f
                } else {
                    selectedItems.add(item)
                    itemView.setBackgroundColor(Color.LTGRAY)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        itemView.elevation = 12f
                }
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