package org.thecoders.smarttable.ui.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_subject.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.fragments.Fragment_EditSubject

class Activity_EditSubject : AppCompatActivity() {

    lateinit var mEditSubjectFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_subject)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(activity_editsubject_content != null) {

            if(savedInstanceState != null)
                return

            mEditSubjectFragment = Fragment_EditSubject()
            //mEditSubjectFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_editsubject_content, mEditSubjectFragment)
                    .commit()
        }

    }

}
