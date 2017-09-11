package org.thecoders.smarttable.ui


import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import butterknife.*
import org.thecoders.smarttable.R
import org.thecoders.smarttable.viewmodel.LessonViewModel


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Timetable : Fragment() {

    @BindView(R.id.timetable_listview) lateinit var mListView: ListView

    @BindView(R.id.timetable_mon) lateinit var btn_mon: Button
    @BindView(R.id.timetable_tue) lateinit var btn_tue: Button
    @BindView(R.id.timetable_wed) lateinit var btn_wed: Button
    @BindView(R.id.timetable_thu) lateinit var btn_thu: Button
    @BindView(R.id.timetable_fri) lateinit var btn_fri: Button
    @BindView(R.id.timetable_more) lateinit var more: Button


    private lateinit var mViewModel: LessonViewModel

    companion object {
        private lateinit var mLessonAdapter: Adapter_Lesson
        private lateinit var unbinder: Unbinder
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(LessonViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_timetable, container, false) as View

        unbinder = ButterKnife.bind(this, rootView)

        mLessonAdapter = Adapter_Lesson(
                context = activity,
                layoutResourceId = R.layout.listview_item_lesson,
                data = mutableListOf(),
                enableEdit = true
        )

        mListView.adapter = mLessonAdapter

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder.unbind()
    }

    @OnClick(R.id.timetable_mon)
    fun onClickMon() {
        LessonViewModel.Companion.LoadLessonsIntoAdapter(mLessonAdapter, "monday", mViewModel)
    }

    @OnLongClick(R.id.timetable_mon)
    fun onLongClickMon(): Boolean {
        val intent = Intent(context, Activity_ModifyDay::class.java)
        intent.putExtra("day", "monday")
        startActivity(intent)
        return true
    }

    @OnClick(R.id.timetable_more)
    fun onClickMore() {
        startActivity(Intent(context, Activity_CreateSubject::class.java))
    }
}
