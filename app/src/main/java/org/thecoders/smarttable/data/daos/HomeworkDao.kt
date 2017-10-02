package org.thecoders.smarttable.data.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import org.thecoders.smarttable.data.DateConverter
import org.thecoders.smarttable.data.pojos.Homework
import java.util.*

/**
 * Created by frenz on 12.06.2017.
 */

@Dao
interface HomeworkDao {
    @Query("select * from homework")
    fun loadHomework(): LiveData<List<Homework>>

    @Query("select * from homework where id = :id")
    fun loadHomeworkById(id: Long): Homework

    //TODO: Query by subject
    //TODO: Query by effort
    //TODO: Query by state(finished?)
    //TODO: Query by deadline

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(homework: Homework)

    @Insert
    fun insertMany(homeworks: List<Homework>)

    @Query("update homework" +
            " set id = :id, subject = :subject, task = :task, date_start = :date_start," +
            " date_deadline = :date_deadline, finished = :finished, effort = :effort" +
            " where id = :id")
    @TypeConverters(DateConverter::class)
    fun updateById(id: Long, subject: String, task: String, date_start: Date, date_deadline: Date,
                   finished: Boolean, effort: String)

    @Delete
    fun delete(homework: Homework)
}