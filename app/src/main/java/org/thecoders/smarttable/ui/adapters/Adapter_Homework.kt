package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.ui.dialogs.Dialog_ConfirmDelete
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
class Adapter_Homework(val context: Context, var data: MutableList<Homework>, val enableEdit: Boolean) :
        RecyclerView.Adapter<Adapter_Homework.HomeworkViewHolder>() {

    companion object {
        private val LOG_TAG = Adapter_Homework::class.java.simpleName
    }

    inner class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val mSubject: TextView = itemView.findViewById(R.id.item_homework_subject)
            val mTask: TextView = itemView.findViewById(R.id.item_homework_task)
            val mDeadline: TextView = itemView.findViewById(R.id.item_homework_deadline)
            val mDeleteIcon: ImageView = itemView.findViewById(R.id.item_homework_delete)

            val homework = data[position]

            val today = DateConverter.dateFormat.format(Date())
            val deadline = DateConverter.dateFormat.format(homework.date_deadline)

            val timeToDeadline = DateConverter().getDifference(today, deadline)

            when {
                homework.finished -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.done))
                timeToDeadline <= 1 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority4))
                timeToDeadline <= 3 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority3))
                timeToDeadline <= 7 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority2))
                timeToDeadline <= 14 -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority1))
                else -> itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.homework_priority0))
            }

            Log.v(LOG_TAG, homework.toString())

            mSubject.text = "${homework.subject}: "
            mTask.text = "${homework.task} (${homework.effort})"
            mDeadline.text = "$timeToDeadline days left ($deadline)"

            if (enableEdit) {
                mDeleteIcon.setOnClickListener { displayDeleteDialog(homework, context) }
            } else {
                mDeleteIcon.isClickable = false
                mDeleteIcon.visibility = View.INVISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.listview_item_homework,
                parent,
                false
        )

        return HomeworkViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = data.count()


    private fun displayDeleteDialog(homework: Homework, context: Context) {
        val confirmDeleteDialog = Dialog_ConfirmDelete()
        confirmDeleteDialog.objectToDelete = homework
        confirmDeleteDialog.objectAdapter = this
        confirmDeleteDialog.show((context as AppCompatActivity).supportFragmentManager, LOG_TAG)
    }

    fun alterItems(newList: List<Homework>) {
        data = newList.toMutableList()
        notifyDataSetChanged()
    }
}
