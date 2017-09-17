package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.listview_item_lesson.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Lesson
import org.thecoders.smarttable.helpers.LessonItemTouchHelperAdapter
import org.thecoders.smarttable.helpers.LessonItemTouchHelperViewHolder
import org.thecoders.smarttable.helpers.TimeHelper
import java.util.*

/**
 * Created by frenz on 30.07.2017.
 */

class Adapter_Lesson(val context: Context, var data: MutableList<Lesson>, val enableEdit: Boolean) :
        RecyclerView.Adapter<Adapter_Lesson.LessonHolder>(), LessonItemTouchHelperAdapter {

    companion object {
        private val LOG_TAG = Adapter_Lesson::class.java.simpleName
    }

    inner class LessonHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LessonItemTouchHelperViewHolder {

        fun bind(position: Int) {
            val lesson = data[position]
            val mTimespan = itemView.item_lesson_timespan
            val mSubject = itemView.item_lesson_subject
            val mDeleteIcon = itemView.item_lesson_delete

            mTimespan.text = lesson.timing
            mSubject.text = lesson.subjectName

            if(enableEdit) {
                mDeleteIcon.setOnClickListener {
                    data.removeAt(position)
                    notifyItemRemoved(position)
                }
            } else {
                mDeleteIcon.isClickable = false
                mDeleteIcon.visibility = View.INVISIBLE
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.listview_item_lesson,
                parent,
                false
        )

        return LessonHolder(itemView)
    }

    override fun onBindViewHolder(holder: LessonHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = data.count()

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if(fromPosition < toPosition) {
            for (i in fromPosition until toPosition) Collections.swap(data, i, i + 1)
            data[fromPosition].id--
            data[toPosition].id++
        } else {
            for (i in fromPosition downTo toPosition + 1) Collections.swap(data, i, i - 1)
            data[fromPosition].id++
            data[toPosition].id--
        }

        data[fromPosition].timing = TimeHelper.calcTimingById(context, data.indexOf(data[fromPosition]))
        data[toPosition].timing = TimeHelper.calcTimingById(context, data.indexOf(data[toPosition]))

        notifyItemChanged(fromPosition)
        notifyItemChanged(toPosition)

        notifyItemMoved(fromPosition, toPosition)

        return true
    }

    fun alterItems(newList: List<Lesson>) {
        data = newList.toMutableList()
        notifyDataSetChanged()
    }
}