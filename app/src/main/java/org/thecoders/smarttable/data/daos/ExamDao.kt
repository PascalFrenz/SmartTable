package org.thecoders.smarttable.data.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.thecoders.smarttable.data.pojos.Exam

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
    fun insertOrUpdate(exam: Exam)

    @Insert
    fun insertMany(exams: List<Exam>)

    @Delete
    fun delete(exam: Exam)
}