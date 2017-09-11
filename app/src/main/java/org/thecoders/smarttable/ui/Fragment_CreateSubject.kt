package org.thecoders.smarttable.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.color.SimpleColorWheelDialog
import org.thecoders.smarttable.R
import org.thecoders.smarttable.data.Subject

/**
 * Created by frenz on 29.06.2017.
 */

class Fragment_CreateSubject : Fragment(), SimpleDialog.OnDialogResultListener {

    /**
     * Establishes a communication from Fragment to Activity. It is used to pass
     * information over to the hosting activity.
     * That could be information about the Fragment's state or some information
     * entered by the user in the UI
     */
    interface OnAddSubjectPressedListener {
        /**
         * @param subject The Subject object that is supposed to be added to the atabase
         */
        fun onAddSubjectRequested(subject: Subject)
    }

    @BindView(R.id.createsubject_name) lateinit var mName: EditText
    @BindView(R.id.createsubject_teacher) lateinit var mTeacher: EditText
    @BindView(R.id.createsubject_category) lateinit var mCategory: AutoCompleteTextView
    @BindView(R.id.createsubject_color_view) lateinit var mColorView: TextView
    @BindView(R.id.createsubject_setcolor_btn) lateinit var mColorButton: Button
    @BindView(R.id.createsubject_add_fab) lateinit var mFab: FloatingActionButton

    private lateinit var mUnbinder: Unbinder
    private lateinit var mCallback: OnAddSubjectPressedListener

    private val COLOR_DIALOG: String = "ColorPickerDialog"
    private var mColor: Int = 0

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mCallback = try {
            activity as OnAddSubjectPressedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnAddSubjectPressedListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_create_subject, container, false) as View
        mUnbinder = ButterKnife.bind(this, rootView)
        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder.unbind()
    }

    @OnClick(R.id.createsubject_setcolor_btn)
    fun setLessonColor() {
        SimpleColorWheelDialog.build()
                .alpha(false)
                .color(Color.BLUE)
                .show(this, COLOR_DIALOG)
    }

    @OnClick(R.id.createsubject_add_fab)
    fun addLessonToDatabase() {
        //Checks for missing information for the creation of a Subject object.
        //If some information is missing, the user is notified via a Toast and nothing happens.
        if (mName.text.toString() == "" || mTeacher.text.toString() == ""
                || mCategory.text.toString() == "" || mColor == 0) {

            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()

        } else {
            //This passes the values entered in the fields of the fragment to the
            //hosting activity, bundled in a Subject object. The activity then will
            //process the given information.
            //Most likely it will insert the Subject into the Database
            mCallback.onAddSubjectRequested(Subject(
                    id = 0,
                    name = mName.text.toString().trim(),
                    teacher = mTeacher.text.toString().trim(),
                    category = mCategory.text.toString().trim(),
                    color = mColor
            ))
        }
    }

    //Handles the color information that is returned by the SimpleColorDialog
    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        if (COLOR_DIALOG == dialogTag && which == SimpleColorDialog.BUTTON_POSITIVE) {
            mColor = extras.getInt(SimpleColorWheelDialog.COLOR)
            mColorView.text = ""
            mColorView.setBackgroundColor(mColor)
            return true
        }
        return false
    }
}