package org.thecoders.smarttable.ui.fragments

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
import kotlinx.android.synthetic.main.fragment_homeworklist.view.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.activities.Activity_CreateHomework
import org.thecoders.smarttable.ui.adapters.Adapter_Homework
import org.thecoders.smarttable.viewmodel.HomeworkViewModel

/**
 * Created by Pascal on 23.03.2016.

 * This fragment containes a ListView that holds all the Homework as Items. When the app is started,
 * it is attached to a SectionsPagerAdapter in the [org.thecoders.smarttable.Activity_Main].
 */

class Fragment_Homeworklist : Fragment() {

    private lateinit var mHomeworkViewModel: HomeworkViewModel
    private lateinit var mHomeworkAdapter: Adapter_Homework
    var mSharedFab: FloatingActionButton? = null

    private lateinit var mUnbinder: Unbinder
    @BindView(R.id.homeworklist_listview) lateinit var mHomeworkListView: RecyclerView
    @BindView(R.id.homeworklist_swiperefreshlayout) lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    companion object {
        private val LOG_TAG = Fragment_Homeworklist::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHomeworkViewModel = ViewModelProviders.of(this).get(HomeworkViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_homeworklist, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)

        mHomeworkAdapter = Adapter_Homework(activity, mutableListOf(), true)

        mHomeworkViewModel.homeworkList.observe(this, Observer {
            if (it != null) {
                mHomeworkAdapter.alterItems(it)
            }
        })

        mHomeworkListView.adapter = mHomeworkAdapter


        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        mHomeworkListView.layoutManager = layoutManager

        mSwipeRefreshLayout.setOnRefreshListener {
            Toast.makeText(context, "Finished refreshing items",
                    Toast.LENGTH_SHORT).show()
            rootView.homeworklist_swiperefreshlayout.isRefreshing = false
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
                startActivity(Intent(context, Activity_CreateHomework::class.java))
            }
        }
    }
}