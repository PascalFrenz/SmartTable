package org.thecoders.smarttable.ui

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_subject.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.Subject
import org.thecoders.smarttable.viewmodel.SubjectViewModel

/**
 * Created by frenz on 29.06.2017.
 */
class Activity_CreateSubject : AppCompatActivity(), Fragment_CreateSubject.OnAddSubjectPressedListener {

    private lateinit var mSubjectViewModel: SubjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)

        if(activity_createsubject_content != null) {
            if(savedInstanceState != null)
                return

            val createLessonFragment = Fragment_CreateSubject()

            createLessonFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_createsubject_content, createLessonFragment)
                    .commit()
        }
    }

    override fun onAddSubjectRequested(subject: Subject) {
        mSubjectViewModel.addSubject(subject)
        NavUtils.navigateUpFromSameTask(this)
    }
}