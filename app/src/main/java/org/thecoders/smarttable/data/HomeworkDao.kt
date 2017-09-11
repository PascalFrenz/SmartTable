package org.thecoders.smarttable.data

import android.arch.persistence.room.*

/**
 * Created by frenz on 12.06.2017.
 */

@Dao
interface HomeworkDao {
    @Query("select * from homework")
    fun loadHomework(): List<Homework>

    @Query("select * from homework where id = :id")
    fun loadHomeworkById(id: Long): Homework

    //TODO: Query by subject
    //TODO: Query by effort
    //TODO: Query by state(finished?)
    //TODO: Query by deadline

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(homework: Homework)

    @Insert
    fun insertMany(homeworks: List<Homework>)

    @Delete
    fun delete(homework: Homework)
}