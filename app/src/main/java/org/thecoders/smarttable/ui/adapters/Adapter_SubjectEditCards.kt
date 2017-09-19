package org.thecoders.smarttable.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Subject
import org.thecoders.smarttable.viewmodel.SubjectViewModel

/**
 * Created by frenz on 19.09.2017.
 */
class Adapter_SubjectEditCards(private var data: MutableList<Subject>, private val mSubjectViewModel: SubjectViewModel) : RecyclerView.Adapter<Adapter_SubjectEditCards.SubjectEditCardViewHolder>() {

    inner class SubjectEditCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(position: Int) {
            val mSubject: TextView = itemView.findViewById(R.id.item_subject_editcard_subject)
            val mTeacher: TextView = itemView.findViewById(R.id.item_subject_editcard_teacher)
            val mCategory: TextView = itemView.findViewById(R.id.item_subject_editcard_category)
            val mEditButton: Button = itemView.findViewById(R.id.item_subject_editcard_btn_edit)
            val mDeleteButton: Button = itemView.findViewById(R.id.item_subject_editcard_btn_delete)

            val subject = data[position]

            mSubject.text = subject.name
            mSubject.setTextColor(subject.color)
            mTeacher.text = subject.teacher
            mCategory.text = subject.category

            mEditButton.setOnClickListener {
                //Let a dialog pop up or something
            }

            mDeleteButton.setOnClickListener {
                data.removeAt(position)
                notifyItemRemoved(position)
                mSubjectViewModel.deleteSubject(subject)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectEditCardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_item_subject_editcard,
                parent,
                false
        )

        return SubjectEditCardViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.count()

    override fun onBindViewHolder(holder: SubjectEditCardViewHolder, position: Int) = holder.bind(position)

    fun alterItems(newList: List<Subject>) {
        data = newList.toMutableList()
        notifyDataSetChanged()
    }

}