package org.thecoders.smarttable.ui.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Exam
import java.util.*


class EditExamFragment : Fragment() {

    private lateinit var mUnbinder: Unbinder
    private lateinit var mExam: Exam

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mExam = assembleExam(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_exam, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder.unbind()
    }

    private fun assembleExam(bundle: Bundle): Exam {
        return Exam(
                id = bundle.getLong(Exam.ID),
                subject = bundle.getString(Exam.SUBJECT),
                topic = bundle.getString(Exam.TOPIC),
                date = bundle.getSerializable(Exam.DATE) as Date,
                grade = bundle.getString(Exam.GRADE)
        )
    }

}
