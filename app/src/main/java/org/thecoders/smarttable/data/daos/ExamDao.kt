package org.thecoders.smarttable.data.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Exam
import java.util.*

/**
 * Created by frenz on 24.06.2017.
 */

@Dao

interface ExamDao {

    @Query("select * from exam")
    fun loadExams(): LiveData<List<Exam>>

    @Query("select * from exam where id = :id")
    fun loadExamById(id: Long): Exam

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(exam: Exam)

    @Insert
    fun insertMany(exams: List<Exam>)


    @Query("update exam" +
            " set id = :id, subject = :subject, topic = :topic, date = :date, grade = :grade" +
            " where id = :id")
    @TypeConverters(DateConverter::class)
    fun updateById(id: Long, subject: String, topic: String, date: Date, grade: String)

    @Delete
    fun delete(exam: Exam)
}