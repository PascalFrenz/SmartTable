package org.thecoders.smarttable.ui.dialogs

import android.app.DialogFragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.activities.Activity_CreateSubject

/**
 * Created by frenz on 18.09.2017.
 */
class Dialog_SubjectActions : DialogFragment() {

    private lateinit var mUnbinder: Unbinder

    @BindView(R.id.subjectactions_btn_new) lateinit var newSubject: Button
    @BindView(R.id.subjectactions_btn_edit) lateinit var editSubject: Button
    @BindView(R.id.subjectactions_btn_delete) lateinit var deleteSubject: Button

    companion object {
        fun newInstance(): Dialog_SubjectActions {
            return Dialog_SubjectActions()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater?.inflate(R.layout.fragment_dialog_subject_actions, container) as View
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mUnbinder = ButterKnife.bind(this, view)


    }

    override fun onDestroy() {
        super.onDestroy()
        mUnbinder.unbind()
    }

    @OnClick(R.id.subjectactions_btn_new)
    fun onClickNewSubject() {
        startActivity(Intent(activity, Activity_CreateSubject::class.java))
        dismiss()
    }

    @OnClick(R.id.subjectactions_btn_edit)
    fun onClickEditSubject() {
        dismiss()
    }

    @OnClick(R.id.subjectactions_btn_delete)
    fun onClickDeleteSubject() {
        dismiss()
    }
}