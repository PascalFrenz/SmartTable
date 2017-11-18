package org.thecoders.smarttable.ui.fragments

import android.arch.lifecycle.Observer
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
import org.thecoders.smarttable.ui.activities.CreateHomeworkActivity
import org.thecoders.smarttable.ui.adapters.HomeworkAdapter
import org.thecoders.smarttable.viewmodel.HomeworkViewModel
import java.lang.ref.WeakReference

/**
 * Created by Pascal on 23.03.2016.

 * This fragment containes a ListView that holds all the Homework as Items. When the app is started,
 * it is attached to a SectionsPagerAdapter in the [org.thecoders.smarttable.Activity_Main].
 */

class HomeworklistFragment : Fragment() {

    private lateinit var mHomeworkViewModel: HomeworkViewModel
    private lateinit var mHomeworkAdapter: HomeworkAdapter
    private var mSharedFab: FloatingActionButton? = null

    private lateinit var mUnbinder: Unbinder
    @BindView(R.id.homeworklist_listview) lateinit var mHomeworkListView: RecyclerView
    @BindView(R.id.homeworklist_swiperefreshlayout) lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    companion object {
        private val LOG_TAG = HomeworklistFragment::class.java.simpleName

        fun newInstance(homeworkViewModel: HomeworkViewModel): HomeworklistFragment {
            val homeworklistFragment = HomeworklistFragment()
            homeworklistFragment.mHomeworkViewModel = homeworkViewModel
            return homeworklistFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_homeworklist, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)

        val onHomeworkEditClickCallback =
                try {
                    activity as HomeworkAdapter.OnHomeworkAdapterActionListener
                } catch (e: ClassCastException) {
                    throw ClassCastException(activity.toString() +
                            " must implement HomeworkAdapter.OnHomeworkAdapterActionListener!")
                }

        mHomeworkAdapter = HomeworkAdapter(WeakReference(activity), mutableListOf(), true, onHomeworkEditClickCallback)

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
                startActivity(Intent(context, CreateHomeworkActivity::class.java))
            }
        }
    }
}