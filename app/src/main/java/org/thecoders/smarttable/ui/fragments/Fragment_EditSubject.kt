package org.thecoders.smarttable.ui.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import org.thecoders.smarttable.R
import org.thecoders.smarttable.ui.adapters.Adapter_SubjectEditCards
import org.thecoders.smarttable.viewmodel.SubjectViewModel

/**
 * A placeholder fragment containing a simple view.
 */

class Fragment_EditSubject : Fragment() {

    private lateinit var mSubjectViewModel: SubjectViewModel


    private lateinit var mUnbinder: Unbinder
    @BindView(R.id.editsubject_recyclerview) lateinit var mRecyclerView: RecyclerView

    private lateinit var mRecyclerAdapter: Adapter_SubjectEditCards

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSubjectViewModel = ViewModelProviders.of(this).get(SubjectViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit_subject, container, false)

        mUnbinder = ButterKnife.bind(this, rootView)
        mRecyclerAdapter = Adapter_SubjectEditCards(mutableListOf(), mSubjectViewModel)

        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.adapter = mRecyclerAdapter

        mSubjectViewModel.subjectList.observe(this, Observer {
            if(it != null)
                mRecyclerAdapter.alterItems(it)
        })

        return rootView
    }
}
