package org.thecoders.smarttable.ui.adapters

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Subject
import org.thecoders.smarttable.ui.dialogs.ConfirmDeleteDialog


/**
 * Created by frenz on 24.06.2017.
 */
class SubjectAdapter(val context: Context, var data: MutableList<Subject>, private val enableEdit: Boolean, private val dbEdit: Boolean) :
        RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    companion object {
        private val LOG_TAG = SubjectAdapter::class.java.simpleName
    }

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val mName: TextView = itemView.findViewById(R.id.item_subject_name)
            val mTeacher: TextView = itemView.findViewById(R.id.item_subject_teacher)
            val mCategory: TextView = itemView.findViewById(R.id.item_subject_category)
            val mDeleteIcon: ImageView = itemView.findViewById(R.id.item_subject_delete)

            val subject = data[position]

            mName.text = subject.name
            mTeacher.text = "(${subject.teacher})"
            mCategory.text = subject.category
            if (enableEdit) {
                if (!dbEdit) {
                    mDeleteIcon.setOnClickListener {
                        data.removeAt(position)
                        notifyItemRemoved(position)
                    }
                } else {
                    mDeleteIcon.setOnClickListener { displayDeleteDialog(subject, context) }
                }
            } else {
                mDeleteIcon.isClickable = false
                mDeleteIcon.visibility = View.INVISIBLE
            }

            itemView.setBackgroundColor(subject.color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.listview_item_subject,
                parent,
                false
        )

        return SubjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = data.count()


    private fun displayDeleteDialog(subject: Subject, context: Context) {
        val confirmDeleteDialog = ConfirmDeleteDialog()
        confirmDeleteDialog.objectToDelete = subject
        confirmDeleteDialog.objectAdapter = this
        confirmDeleteDialog.show((context as AppCompatActivity).supportFragmentManager, LOG_TAG)
    }

    fun alterItems(newList: List<Subject>) {
        data = newList.toMutableList()
        notifyDataSetChanged()
    }


}