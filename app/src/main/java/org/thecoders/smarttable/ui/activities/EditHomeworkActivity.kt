package org.thecoders.smarttable.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_homework.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.fragments.EditHomeworkFragment

class EditHomeworkActivity : AppCompatActivity() {

    private lateinit var mEditHomeworkFragment: EditHomeworkFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_homework)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(activity_edithomework_content != null) {

            if(savedInstanceState != null)
                return

            mEditHomeworkFragment = EditHomeworkFragment()
            mEditHomeworkFragment.arguments = intent.extras

            supportFragmentManager.beginTransaction()
                    .add(R.id.activity_edithomework_content, mEditHomeworkFragment)
                    .commit()

        }
    }
}
