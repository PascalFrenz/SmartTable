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
import org.thecoders.smarttable.data.pojos.Homework
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class EditHomeworkFragment : Fragment() {

    private lateinit var mUnbinder: Unbinder

    //Homework that is supposed to be edited
    private lateinit var mHomework: Homework

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mHomework = assembleHomework(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_edit_homework, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)



        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()

        mUnbinder.unbind()
    }

    private fun assembleHomework(bundle: Bundle): Homework {
        return Homework(
                bundle.getLong(Homework.ID),
                bundle.getString(Homework.SUBJECT),
                bundle.getString(Homework.TASK),
                bundle.getSerializable(Homework.START) as Date,
                bundle.getSerializable(Homework.DEADLINE) as Date,
                bundle.getBoolean(Homework.FINISHED),
                bundle.getString(Homework.EFFORT)
        )
    }
}
