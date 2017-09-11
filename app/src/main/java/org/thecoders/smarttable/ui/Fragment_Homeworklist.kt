package org.thecoders.smarttable.ui

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_homeworklist.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.Homework
import org.thecoders.smarttable.viewmodel.HomeworkViewModel

/**
 * Created by Pascal on 23.03.2016.

 * This fragment containes a ListView that holds all the Homework as Items. When the app is started,
 * it is attached to a SectionsPagerAdapter in the [org.thecoders.smarttable.Activity_Main].
 */

class Fragment_Homeworklist : Fragment() {

    private lateinit var mHomeworkViewModel: HomeworkViewModel
    var mSharedFab: FloatingActionButton? = null

    companion object {
        private val LOG_TAG = Fragment_Homeworklist::class.java.simpleName

        private lateinit var mHomeworkAdapter: Adapter_Homework

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeworkViewModel = ViewModelProviders.of(this).get(HomeworkViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_homeworklist, container, false) as View

        mHomeworkAdapter = Adapter_Homework(
                context = activity,
                layoutRecourceId = R.layout.listview_item_homework,
                data = mutableListOf(),
                enableEdit = true
        )

        LoadHomeworkItems(mHomeworkViewModel).execute()

        val listView = rootView.homeworklist_listview
        listView.adapter = mHomeworkAdapter


        rootView.homeworklist_swiperefreshlayout.setOnRefreshListener {
            Toast.makeText(context, "Finished refreshing items",
                    Toast.LENGTH_SHORT).show()
            rootView.homeworklist_swiperefreshlayout.isRefreshing = false
        }

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        mSharedFab = null
    }

    fun shareFab(fab: FloatingActionButton?) {
        if(fab == null) {
            if(mSharedFab != null) mSharedFab?.setOnClickListener(null)
            mSharedFab = null
        } else {
            mSharedFab = fab
            mSharedFab?.setImageResource(R.drawable.ic_add_24px)
            mSharedFab?.setOnClickListener {
                Log.v(LOG_TAG, "FloatingActionButton pressed!")
                startActivity(Intent(context, Activity_CreateHomework::class.java))
            }
        }
    }

    class LoadHomeworkItems(private val homeworkViewModel: HomeworkViewModel) : AsyncTask<String, Int, List<Homework>>() {

        override fun doInBackground(vararg params: String?): List<Homework>
                = homeworkViewModel.loadHomeworkList()

        override fun onPostExecute(result: List<Homework>?) {
            if(result != null) {
                mHomeworkAdapter.clear()
                for (homework in result) mHomeworkAdapter.add(homework)
            }
        }
    }
}