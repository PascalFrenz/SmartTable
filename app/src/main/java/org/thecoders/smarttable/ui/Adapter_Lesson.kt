package org.thecoders.smarttable.ui

import android.content.Context
import android.graphics.Rect
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import kotlinx.android.synthetic.main.listview_item_lesson.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.RearrangeableListView
import org.thecoders.smarttable.TimeHelper
import org.thecoders.smarttable.data.Lesson

/**
 * Created by frenz on 30.07.2017.
 */

class Adapter_Lesson(context: Context, val layoutResourceId: Int,
                     var data: MutableList<Lesson>, val enableEdit: Boolean) :
        ArrayAdapter<Lesson>(context, layoutResourceId, data), RearrangeableListView.RearrangeListener {

    private class LessonHolder {
        lateinit var mTimespan: TextView
        lateinit var mSubject: TextView
        lateinit var mTeacher: TextView
        lateinit var mDragspace: Space
        lateinit var mDeleteIcon: ImageView
    }

    companion object {
        private val LOG_TAG = Adapter_Lesson::class.java.simpleName
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        val holder: LessonHolder

        if(row == null) {
            holder = LessonHolder()
            row = LessonView(context, holder)

            initHolder(holder, row)
            row.tag = holder
        } else {
            holder = row.tag as LessonHolder
        }

        val lesson = data[position]

        setupHolder(holder, lesson, row)

        return row
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()

        data.forEach {
            it.id = getPosition(it).toLong()
            it.timing = TimeHelper().calcTimingById(context, it.id.toInt())
        }

//        var i = 0
//        while(i < count) {
//            val lesson = getItem(i)
//            lesson.id = i.toLong()
//            lesson.timing = TimeHelper().calcTimingById(context, i)
//            Log.v(LOG_TAG, lesson.toString())
//            i++
//        }

    }

    private fun initHolder(holder: LessonHolder, row: View) {
        holder.mTimespan = row.item_lesson_timespan
        holder.mSubject = row.item_lesson_subject
        holder.mTeacher = row.item_lesson_teacher
        holder.mDragspace = row.item_lesson_dragspace
        holder.mDeleteIcon = row.item_lesson_delete
    }

    private fun setupHolder(holder: LessonHolder, lesson: Lesson, row: View) {
        holder.mTimespan.text = lesson.timing
        holder.mSubject.text = lesson.subjectName

        if(enableEdit) {
            holder.mDeleteIcon.setOnClickListener { remove(lesson) }
        } else {
            holder.mDeleteIcon.isClickable = false
            holder.mDeleteIcon.visibility = View.INVISIBLE
        }
    }

    override fun onGrab(index: Int) { }

    override fun onRearrangeRequested(fromIndex: Int, toIndex: Int): Boolean {
        if(toIndex in 0..(count - 1)) {
            val dragItem = getItem(fromIndex)

            remove(dragItem)
            insert(dragItem, toIndex)

            notifyDataSetChanged()

            return true
        }
        return false
    }

    override fun onDrop() { }

    private inner class LessonView(context: Context, internal val holder: LessonHolder) :
            ConstraintLayout(context), RearrangeableListView.MovableView {

        init {
            View.inflate(context, R.layout.listview_item_lesson, this)
        }

        override fun onGrabAttempt(x: Int, y: Int): Boolean {
            val dragSpace = Rect()
            holder.mDragspace.getHitRect(dragSpace)
            return dragSpace.contains(x, y)
        }

        override fun onRelease() {

        }

    }
}