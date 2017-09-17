package org.thecoders.smarttable.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.thecoders.smarttable.ui.fragments.Fragment_Settings

/**
 * Created by frenz on 12.07.2017.
 */

class Activity_Settings : AppCompatActivity() {

    companion object {
        private val LOG_TAG = Activity_Settings::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction()
                .replace(android.R.id.content, Fragment_Settings())
                .commit()
    }
}