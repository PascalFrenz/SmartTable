package org.thecoders.smarttable.ui

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.Subject


/**
 * Created by frenz on 24.06.2017.
 */
class Adapter_Subject(context: Context, private val layoutResourceId: Int,
                      val data: List<Subject>, private val enableEdit: Boolean, private val dbEdit: Boolean) :
        ArrayAdapter<Subject>(context, layoutResourceId, data) {

    private class SubjectHolder {
        lateinit var mName: TextView
        lateinit var mTeacher: TextView
        lateinit var mCategory: TextView
        lateinit var mDeleteIcon: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var row = convertView
        val holder: SubjectHolder

        if(row == null) {
            val inflater = (context as AppCompatActivity).layoutInflater

            row = inflater.inflate(layoutResourceId, parent, false)
            holder = SubjectHolder()

            initHolder(holder, row)
            row.tag = holder
        } else {
            holder = row.tag as SubjectHolder
        }

        val lesson = data[position]

        setupHolder(holder, lesson, row!!)

        return row
    }

    private fun initHolder(holder: SubjectHolder, row: View) {
        holder.mName = row.findViewById(R.id.item_subject_name)
        holder.mCategory = row.findViewById(R.id.item_subject_category)
        holder.mDeleteIcon = row.findViewById(R.id.item_subject_delete)
    }

    private fun setupHolder(holder: SubjectHolder, subject: Subject?, row: View) {
        if (subject != null) {
            holder.mName.text = subject.name
            holder.mTeacher.text = "(${subject.teacher})"
            holder.mCategory.text = subject.category
            if (enableEdit) {
                if (!dbEdit) {
                    holder.mDeleteIcon.setOnClickListener {
                        remove(subject)
                        notifyDataSetChanged()
                    }
                } else {
                    holder.mDeleteIcon.setOnClickListener { displayDeleteDialog(subject, context) }
                }
            } else {
                holder.mDeleteIcon.isClickable = false
                holder.mDeleteIcon.visibility = View.INVISIBLE
            }

            row.setBackgroundColor(subject.color)
        }
    }

    private fun displayDeleteDialog(subject: Subject, context: Context) {
        val confirmDeleteDialog = Dialog_ConfirmDelete()
        confirmDeleteDialog.objectToDelete = subject
        confirmDeleteDialog.objectAdapter = this
        confirmDeleteDialog.show((context as AppCompatActivity).supportFragmentManager, LOG_TAG)
    }

    companion object {
        private val LOG_TAG = Adapter_Subject::class.java.simpleName
    }
}