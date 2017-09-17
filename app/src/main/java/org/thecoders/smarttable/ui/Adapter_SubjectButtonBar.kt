package org.thecoders.smarttable.ui

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.thecoders.smarttable.R

/**
 * Created by frenz on 14.09.2017.
 */
class Adapter_SubjectButtonBar(var data: MutableList<String>)
    : RecyclerView.Adapter<Adapter_SubjectButtonBar.SubjectViewHolder>() {

    class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(subjectName: String) {
            val mButton: Button = itemView.findViewById(R.id.item_subjectbutton_button)
            mButton.text = subjectName
            mButton.setOnLongClickListener {
                it.tag = subjectName

                val item = ClipData.Item(it.tag.toString())
                val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
                val data = ClipData(it.tag.toString(), mimeTypes, item)

                val shadowBuilder = View.DragShadowBuilder(it)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    it.startDragAndDrop(data, shadowBuilder, null, 0)
                } else {
                    @Suppress("DEPRECATION")
                    it.startDrag(data, shadowBuilder, null, 0)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val button = LayoutInflater.from(parent.context)
                .inflate(R.layout.listview_item_subject_button, parent, false)

        return SubjectViewHolder(button)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.count()
    }

    fun alterItems(newList: List<String>) {
        this.data = newList.toMutableList()
        notifyDataSetChanged()
    }
}