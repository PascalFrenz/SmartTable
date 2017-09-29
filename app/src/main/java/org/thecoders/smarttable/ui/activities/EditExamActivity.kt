package org.thecoders.smarttable.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_exam.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.fragments.EditExamFragment

class EditExamActivity : AppCompatActivity() {

    private lateinit var mEditExamFragment: EditExamFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exam)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(activity_editexam_content != null) {
            if(savedInstanceState != null)
                return

            mEditExamFragment = EditExamFragment()
            mEditExamFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_editexam_content, mEditExamFragment)
                    .commit()
        }
    }
}
