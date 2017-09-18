package org.thecoders.smarttable.ui.fragments


import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import kotlinx.android.synthetic.main.fragment_examlist.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.activities.Activity_CreateExam
import org.thecoders.smarttable.ui.adapters.Adapter_Exam
import org.thecoders.smarttable.viewmodel.ExamViewModel


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Examlist : LifecycleFragment() {

    private lateinit var mExamViewModel: ExamViewModel
    private lateinit var mExamAdapter: Adapter_Exam
    var mSharedFab: FloatingActionButton? = null

    private lateinit var mUnbinder: Unbinder
    @BindView(R.id.examlist_listview) lateinit var mExamListView: RecyclerView
    @BindView(R.id.examlist_swiperefreshlayout) lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    companion object {
        private val LOG_TAG = Fragment_Examlist::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mExamViewModel = ViewModelProviders.of(this).get(ExamViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_examlist, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)

        //Create the Adapter
        mExamAdapter = Adapter_Exam(activity, mutableListOf(), true)

        //Let the Adapter change upon change in the ViewModels data
        mExamViewModel.examList.observe(this, Observer {
            if (it != null) {
                mExamAdapter.alterItems(it)
            }
        })

        //Set the ListViews adapter
        mExamListView.adapter = mExamAdapter

        //Set the ListViews LayoutManager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mExamListView.layoutManager = layoutManager



        mSwipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(context, "Finished refreshing items",
                    Toast.LENGTH_SHORT).show()
            rootView.examlist_swiperefreshlayout.isRefreshing = false
        }

        return rootView
    }

    override fun onDestroy() {
        super.onDestroy()
        mSharedFab = null
        mUnbinder.unbind()
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
}
