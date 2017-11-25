package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.helpers.AbstractAdapterInterface
import org.thecoders.smarttable.helpers.TimeHelper
import org.thecoders.smarttable.ui.dialogs.ConfirmDeleteDialog
import java.lang.ref.WeakReference

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
                      val callback: AbstractAdapterInterface) :
        RecyclerView.Adapter<HomeworkAdapter.HomeworkViewHolder>() {

    private val context: Context? by lazy(weakContext::get)

    var multiSelect: Boolean = false
    var selectedItems: MutableList<Homework> = mutableListOf()

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
                this@HomeworkAdapter.callback.onObjectDeleteRequest(it, this@HomeworkAdapter)
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
        private val LOG_TAG = HomeworkAdapter::class.java.simpleName
    }

    inner class HomeworkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val mSubject: TextView = itemView.findViewById(R.id.item_homework_subject)
            val mTask: TextView = itemView.findViewById(R.id.item_homework_task)
            val mDeadline: TextView = itemView.findViewById(R.id.item_homework_deadline)
            val mEditIcon: ImageView = itemView.findViewById(R.id.item_homework_edit)

            val homework = data[position]
            Log.v(LOG_TAG, homework.toString())

            val deadline = DateConverter.dateFormat.format(homework.date_deadline)
            val timeToDeadline = TimeHelper.calcTimeToDeadline(homework.date_deadline)


            if (selectedItems.contains(homework)) {
                itemView.setBackgroundColor(Color.LTGRAY)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    itemView.elevation = 12f
            } else {
                itemView.setBackgroundColor(getHomeworkColor(homework))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    itemView.elevation = 0f
            }


            mSubject.text = "${homework.subject}: "
            mTask.text = "${homework.task} (${homework.effort})"
            mDeadline.text = "$timeToDeadline days left ($deadline)"


            //When editing is enabled, pass an edit request up to
            //the hosting activity
            if (enableEdit) {
                mEditIcon.setOnClickListener { callback.onObjectEditRequest(homework) }
            } else {
                mEditIcon.isClickable = false
                mEditIcon.visibility = View.INVISIBLE
            }

            //Implementing the delete feature here and pass a delete request up to
            //the hosting activity
            itemView.setOnLongClickListener {
                //callback.onObjectDeleteRequest(homework, this@HomeworkAdapter)
                (context as AppCompatActivity).startSupportActionMode(actionModeCallbacks)
                selectItem(homework)
                return@setOnLongClickListener true
            }

            itemView.setOnClickListener {
                selectItem(homework)
            }
        }


        private fun getHomeworkColor(homework: Homework): Int {
            val timeToDeadline = TimeHelper.calcTimeToDeadline(homework.date_deadline)

            return if (homework.finished) {
                ContextCompat.getColor(context, R.color.done)
            } else {
                when {
                    timeToDeadline <= 1 -> ContextCompat.getColor(context, R.color.homework_priority4)
                    timeToDeadline <= 3 -> ContextCompat.getColor(context, R.color.homework_priority3)
                    timeToDeadline <= 7 -> ContextCompat.getColor(context, R.color.homework_priority2)
                    timeToDeadline <= 14 -> ContextCompat.getColor(context, R.color.homework_priority1)
                    else -> ContextCompat.getColor(context, R.color.homework_priority0)
                }
            }


        }

        private fun selectItem(item: Homework) {
            if (multiSelect) {
                if (selectedItems.contains(item)) {
                    selectedItems.remove(item)
                    itemView.setBackgroundColor(getHomeworkColor(item))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        itemView.elevation = 0f
                    }
                } else {
                    selectedItems.add(item)
                    itemView.setBackgroundColor(Color.LTGRAY)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        itemView.elevation = 12f
                    }
                }
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
