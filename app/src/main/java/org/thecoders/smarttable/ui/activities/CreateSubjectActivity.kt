package org.thecoders.smarttable.ui.activities

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_subject.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.pojos.Subject
import org.thecoders.smarttable.ui.fragments.CreateSubjectFragment
import org.thecoders.smarttable.viewmodel.SubjectViewModel

/**
 * Created by frenz on 29.06.2017.
 *
 * Host-Activity for CreateSubjectFragment
 * Also holds an instance of SubjectViewModel for communication with the database.
 *
 * @see SubjectViewModel
 */
class CreateSubjectActivity : AppCompatActivity(), CreateSubjectFragment.OnAddSubjectPressedListener {

    private lateinit var mFragment: CreateSubjectFragment
    private lateinit var mSubjectViewModel: SubjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subject)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)

        if(activity_createsubject_content != null) {
            if(savedInstanceState != null)
                return

            mFragment = CreateSubjectFragment()
            mFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_createsubject_content, mFragment)
                    .commit()
        }
    }

    override fun onAddSubjectRequested(subject: Subject) {
        mSubjectViewModel.addSubject(subject)
        NavUtils.navigateUpFromSameTask(this)
    }
}