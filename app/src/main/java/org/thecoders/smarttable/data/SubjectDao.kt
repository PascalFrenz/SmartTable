package org.thecoders.smarttable.data

import android.arch.persistence.room.*

/**
 * Created by frenz on 24.06.2017.
 */

@Dao
interface SubjectDao {

    @Query("select * from subjects")
    fun loadSubjects(): List<Subject>

    @Query("select * from subjects where id = :id")
    fun loadSubjectById(id: Long): Subject

    @Query("select name from subjects")
    fun loadSubjectNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(subject: Subject)

    @Insert
    fun insertMany(subjects: List<Subject>)

    @Delete
    fun delete(subject: Subject)

}