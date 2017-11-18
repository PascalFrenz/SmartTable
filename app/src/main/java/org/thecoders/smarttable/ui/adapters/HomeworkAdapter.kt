package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
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
import org.thecoders.smarttable.ui.dialogs.ConfirmDeleteDialog
import java.lang.ref.WeakReference
import java.util.*

/**
 * Created by Pascal on 31.03.2016.

 * This is the Adapter for Homeworklists, where Homeworkitems are displayed.
 * It sets up the view and fills it with the Information given to it by
 * the DataArray of the List.

 * The items can be deleted via a delete icon which toggles a dialog, where
 * the user is asked to acknowledge the deletion.
 * When acknowledged, the selected Homework is deleted in the List, as well
 * as in the Database. Look at [ConfirmDeleteDialog] to learn more
 * about the deletion Dialog and at the implementation of the Homework object
 * to learn more about how it is deleted in the DB

 * Wether the option of deletion should be available or not is set in the
 * constructor.

 */
class HomeworkAdapter(weakContext: WeakReference<Context>, var data: MutableList<Homework>, val enableEdit: Boolean,
                      val callback: OnHomeworkAdapterActionListener) :
        RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    private val context: Context? by lazy(weakContext::get)

    /**
     * This interface is responsible for handling UI actions triggered by the user to prevent
     * context leaks of some kind.
     */
    interface OnHomeworkAdapterActionListener {

        /**
         * This method forwards an edit request from the adapter to the hosting activity.
         *
         * @see org.thecoders.smarttable.ui.activities.MainActivity.onHomeworkEditRequest
         */
        fun onHomeworkEditRequest(homework: Homework)

        /**
         * This method forwards a delete request for the given homework to the hosting activity.
         * The activity then opens up a new ConfirmDeleteDialog.
         *
         * @see ConfirmDeleteDialog
         * @see org.thecoders.smarttable.ui.activities.MainActivity.onHomeworkDeleteRequest
         */
        fun onHomeworkDeleteRequest(homework: Homework, adapter: HomeworkAdapter)
    }

    companion object {
        private val LOG_TAG = HomeworkAdapter::class.java.simpleName
    }

    inner class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val mSubject: TextView = itemView.findViewById(R.id.item_homework_subject)
            val mTask: TextView = itemView.findViewById(R.id.item_homework_task)
            val mDeadline: TextView = itemView.findViewById(R.id.item_homework_deadline)
            val mEditIcon: ImageView = itemView.findViewById(R.id.item_homework_edit)

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


            //When editing is enabled, pass an edit request up to
            //the hosting activity
            if (enableEdit) {
                mEditIcon.setOnClickListener { callback.onHomeworkEditRequest(homework) }
            } else {
                mEditIcon.isClickable = false
                mEditIcon.visibility = View.INVISIBLE
            }

            //Implementing the delete feature here and pass a delete request up to
            //the hosting activity
            itemView.setOnLongClickListener {
                callback.onHomeworkDeleteRequest(homework, this@HomeworkAdapter)
                return@setOnLongClickListener true
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

    fun alterItems(newList: List<Homework>) {
        data = newList.toMutableList()
        notifyDataSetChanged()
    }
}
