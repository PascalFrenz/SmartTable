package org.thecoders.smarttable.ui.dialogs

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.ui.dialogs.EditExamDialog.Companion.newInstance
import org.thecoders.smarttable.viewmodel.ExamViewModel
import org.thecoders.smarttable.viewmodel.SubjectViewModel
import java.util.*

/**
 * Used to make changes to an Exam object via a Dialog.
 * It is very important to create an instance of this DialogFragment via the
 * [newInstance] method, otherwise all important variables such as [mSubjectViewModel] or [mExam]
 * are not correctly initialized!
 */
class EditExamDialog : DialogFragment() {

    companion object {
        /**
         * Creates a new instance of [EditExamDialog]
         *
         * @param args Must contain all information of the Exam for reassembly
         * @param subjectViewModel Is needed to fetch available subjects from DB
         * @param examViewModel Is needed for saving the object to DB
         *
         * @see assembleExam
         * @see SubjectViewModel
         * @see ExamViewModel
         *
         */
        fun newInstance(args: Bundle, subjectViewModel: SubjectViewModel, examViewModel: ExamViewModel): EditExamDialog {
            val editExamFragment = EditExamDialog()
            editExamFragment.arguments = args
            editExamFragment.mSubjectViewModel = subjectViewModel
            editExamFragment.mExamViewModel = examViewModel
            return editExamFragment
        }
    }

    /**
     * The [Exam]-Object to be edited
     */
    private lateinit var mExam: Exam

    /**
     * The [SubjectViewModel] used to fetch all possible subjects for the Exam
     */
    private lateinit var mSubjectViewModel: SubjectViewModel

    /**
     * The [ExamViewModel] used to update the existing DB-entry
     */
    private lateinit var mExamViewModel: ExamViewModel

    /**
     * ButterKnife's [Unbinder] used to initialize all Views in this [DialogFragment]
     */
    private lateinit var mUnbinder: Unbinder

    //All Views that need to be initialized are as follows:
    @BindView(R.id.editexam_subject) lateinit var mSubjectSpinner: Spinner
    @BindView(R.id.editexam_task) lateinit var mTopic: TextInputEditText
    @BindView(R.id.editexam_date) lateinit var mDate: EditText
    @BindView(R.id.editexam_grade) lateinit var mGrade: EditText

    /**
     * The Adapter used to populate [mSubjectSpinner] with items
     * Items are fetched from the DB using the [mSubjectViewModel]
     *
     * @see SubjectViewModel
     */
    private lateinit var mSubjectAdapter: ArrayAdapter<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mExam = assembleExam(arguments)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_dialog_edit_exam, container, false)

        //Initializing all Views
        mUnbinder = ButterKnife.bind(this, rootView)

        //Set-up of the Adapter
        mSubjectAdapter = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                mutableListOf()
        )

        //Subscribe to changes in the subjectNamesList inside mSubjectViewModel
        //That way, the SubjectList is always up-to-date with minimal work
        mSubjectViewModel.subjectNamesList.observe(this, android.arch.lifecycle.Observer {
            if(it != null) {
                val newList = it.toMutableList()
                newList.remove(mExam.subject)       //Removing the current subject to...
                mSubjectAdapter.clear()
                mSubjectAdapter.add(mExam.subject)  //... add it here in order to show it on top
                mSubjectAdapter.addAll(newList)     // and then add every other subject
                mSubjectAdapter.notifyDataSetChanged()
            }
        })

        mSubjectSpinner.adapter = mSubjectAdapter

        //Setup of all other fields
        mTopic.setText(mExam.topic)
        mDate.setText(DateConverter.dateFormat.format(mExam.date))
        mGrade.setText(mExam.grade)

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        //Saves resources
        mUnbinder.unbind()
    }

    @OnClick(R.id.editexam_btn_date)
    fun onSetDateClicked() {
        MyDatePickerDialog().show(activity.fragmentManager, "EditExam_datePicker")
    }

    @OnClick(R.id.editexam_btn_save)
    fun onSaveClicked() {
        val updatedExam = Exam(
                mExam.id,
                mSubjectSpinner.selectedItem as String,
                mTopic.text.toString(),
                DateConverter.dateFormat.parse(mDate.text.toString()),
                mGrade.text.toString()
        )
        mExamViewModel.updateExam(updatedExam)
        this.dismiss()
    }

    @OnClick(R.id.editexam_btn_cancel)
    fun onCancelClicked() {
        this.dismiss()
    }

    /**
     * Takes a [Bundle] as parameter and creates an Exam from it's content
     * The String-ID's are defined in [Exam]
     *
     * @param bundle The bundle from which the exam is reassebled
     * @see Exam
     */
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
