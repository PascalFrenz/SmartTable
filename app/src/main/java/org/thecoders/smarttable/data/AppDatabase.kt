package org.thecoders.smarttable.data

/**
 * Created by frenz on 12.06.2017.
 */

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import org.thecoders.smarttable.data.daos.ExamDao
import org.thecoders.smarttable.data.daos.HomeworkDao
import org.thecoders.smarttable.data.daos.SubjectDao
import org.thecoders.smarttable.data.daos.TimetableRowDao
import org.thecoders.smarttable.data.pojos.Exam
import org.thecoders.smarttable.data.pojos.Homework
import org.thecoders.smarttable.data.pojos.Subject
import org.thecoders.smarttable.data.pojos.TimetableRow

@Database(
        entities = arrayOf(Homework::class, Exam::class, Subject::class, TimetableRow::class),
        version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun homeworkModel(): HomeworkDao
    abstract fun examModel(): ExamDao
    abstract fun subjectModel(): SubjectDao
    abstract fun timetableModel(): TimetableRowDao

    companion object {
        private const val DB_NAME = "SmartTable.db"
        private var INSTANCE: AppDatabase? = null

        fun getPersistenceDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room
                        .databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                        .build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            if(INSTANCE != null) INSTANCE = null
        }
    }
}