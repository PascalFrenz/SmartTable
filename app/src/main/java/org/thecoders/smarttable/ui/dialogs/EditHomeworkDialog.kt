package org.thecoders.smarttable.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.ui.dialogs.EditHomeworkDialog.Companion.newInstance
import org.thecoders.smarttable.viewmodel.HomeworkViewModel
import org.thecoders.smarttable.viewmodel.SubjectViewModel
import java.util.*

/**
 * Used to edit Homework items via a Dialog
 * It is very important to create [EditHomeworkDialog] via the [newInstance] method,
 * otherwise all important variables such as [mHomework] or [mHomeworkViewModel] are not
 * initialized correctly!
 */
class EditHomeworkDialog : DialogFragment() {

    companion object {
        /**
         * Creates a new instance of [EditHomeworkDialog]
         *
         * @param bundle holds all information to reassemble the Homework that should be edited
         * @param subjectViewModel used to load all subject items from DB
         * @param homeworkViewModel used to save changes made in the DB
         *
         * @see Bundle
         * @see SubjectViewModel
         * @see HomeworkViewModel
         */
        fun newInstance(bundle: Bundle, subjectViewModel: SubjectViewModel, homeworkViewModel: HomeworkViewModel): EditHomeworkDialog {
            val editHomeworkFragment = EditHomeworkDialog()
            editHomeworkFragment.arguments = bundle
            editHomeworkFragment.mSubjectViewModel = subjectViewModel
            editHomeworkFragment.mHomeworkViewModel = homeworkViewModel
            return editHomeworkFragment
        }
    }



    /**
     * Used to fetch all ]Subjects from DB
     * @see SubjectViewModel
     */
    private lateinit var mSubjectViewModel: SubjectViewModel

    /**
     * Used to save the updated Homework in the DB
     * @see HomeworkViewModel
     */
    private lateinit var mHomeworkViewModel: HomeworkViewModel

    /**
     * [Homework] that is supposed to be edited
     *
     * @see Homework
     */
    private lateinit var mHomework: Homework

    /**
     * The Adapter used to populate [] with items
     * Items are fetched from the DB using the [mSubjectViewModel]
     *
     * @see SubjectViewModel
     */
    private lateinit var mSubjectAdapter: ArrayAdapter<String>

    private lateinit var mUnbinder: Unbinder

    @BindView(R.id.edithomework_subject) lateinit var mSubjectSpinner: Spinner
    @BindView(R.id.edithomework_task) lateinit var mTask: TextInputEditText
    @BindView(R.id.edithomework_date_start) lateinit var mDateStart: EditText
    @BindView(R.id.edithomework_deadline) lateinit var mDeadline: EditText
    @BindView(R.id.edithomework_effort) lateinit var mEffort: EditText
    @BindView(R.id.edithomework_finished) lateinit var mFinished: CheckBox


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mHomework = assembleHomework(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_dialog_edit_homework, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)

        mSubjectAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                mutableListOf()
        )

        mSubjectViewModel.subjectNamesList.observe(this, android.arch.lifecycle.Observer {
            if(it != null) {
                val newList = it.toMutableList()
                newList.remove(mHomework.subject)       //Removing the current subject to...
                mSubjectAdapter.clear()
                mSubjectAdapter.add(mHomework.subject)  //... add it here in order to show it on top
                mSubjectAdapter.addAll(newList)         // and then add every other subject
                mSubjectAdapter.notifyDataSetChanged()
            }
        })

        mSubjectSpinner.adapter = mSubjectAdapter

        mTask.setText(mHomework.task)
        mDateStart.setText(DateConverter.dateFormat.format(mHomework.date_start))
        mDeadline.setText(DateConverter.dateFormat.format(mHomework.date_deadline))
        mEffort.setText(mHomework.effort)
        mFinished.isChecked = mHomework.finished

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder.unbind()
    }

    @OnClick(R.id.edithomework_btn_date_start)
    fun onSetDateStartClicked() {
        MyDatePickerDialog().show(activity.fragmentManager, "EditHomework_dateStartDialog")
    }

    @OnClick(R.id.edithomework_btn_deadline)
    fun onSetDeadlineClicked() {
        MyDatePickerDialog().show(activity.fragmentManager, "EditHomework_dateDeadlineDialog")
    }

    @OnClick(R.id.edithomework_btn_effort)
    fun onSetEffortClicked() {
        MyTimePickerDialog().show(activity.fragmentManager, "EditHomework_timeEffortDialog")
    }

    @OnClick(R.id.edithomework_btn_save)
    fun onSaveClicked() {
        val newHomework = Homework(
                mHomework.id,
                mSubjectSpinner.selectedItem as String,
                mTask.text.toString(),
                DateConverter.dateFormat.parse(mDateStart.text.toString()),
                DateConverter.dateFormat.parse(mDeadline.text.toString()),
                mFinished.isChecked,
                mEffort.text.toString()
        )
        mHomeworkViewModel.updateHomework(newHomework)
        dismiss()
    }

    @OnClick(R.id.edithomework_btn_cancel)
    fun onCancelClicked() {
        dismiss()
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
