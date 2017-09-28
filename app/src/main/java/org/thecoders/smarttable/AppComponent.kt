package org.thecoders.smarttable

import dagger.Component
import org.thecoders.smarttable.ui.fragments.ModifyDayFragment
import org.thecoders.smarttable.viewmodel.ExamViewModel
import org.thecoders.smarttable.viewmodel.HomeworkViewModel
import org.thecoders.smarttable.viewmodel.LessonViewModel
import org.thecoders.smarttable.viewmodel.SubjectViewModel
import javax.inject.Singleton

/**
 * Created by frenz on 22.06.2017.
 */

@Component(
    modules = arrayOf(
            AndroidModule::class,
            DataModule::class
    )
)
@Singleton
interface AppComponent {
    fun inject(into: HomeworkViewModel)
    fun inject(into: ExamViewModel)
    fun inject(into: SubjectViewModel)
    fun inject(into: LessonViewModel)

    fun inject(into: ModifyDayFragment)
}