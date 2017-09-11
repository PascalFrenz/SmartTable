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
import kotlinx.android.synthetic.main.fragment_examlist.view.*

import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.Exam
import org.thecoders.smarttable.viewmodel.ExamViewModel


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Examlist : Fragment() {

    private lateinit var mExamViewModel: ExamViewModel
    var mSharedFab: FloatingActionButton? = null

    companion object {
        private val LOG_TAG = Fragment_Examlist::class.java.simpleName
        private lateinit var mExamAdapter: Adapter_Exam
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mExamViewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_examlist, container, false)!!

        mExamAdapter = Adapter_Exam(
                context = activity,
                layoutResourceId = R.layout.listview_item_exam,
                data = mutableListOf(),
                enableEdit = true
        )

        LoadExamItems(mExamViewModel).execute()

        rootView.examlist_listview.adapter = mExamAdapter

        rootView.examlist_swiperefreshlayout.setOnRefreshListener {
            Toast.makeText(context, "Finished refreshing items",
                    Toast.LENGTH_SHORT).show()
            rootView.examlist_swiperefreshlayout.isRefreshing = false
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
                startActivity(Intent(context, Activity_CreateExam::class.java))
            }
        }
    }

    class LoadExamItems(private val examViewModel: ExamViewModel) : AsyncTask<String, Int, List<Exam>>() {
        override fun doInBackground(vararg p0: String?): List<Exam>
                = examViewModel.loadExamList()

        override fun onPostExecute(result: List<Exam>?) {
            if(result != null) {
                mExamAdapter.clear()
                for (exam in result) mExamAdapter.add(exam)
            }
        }
    }

}
