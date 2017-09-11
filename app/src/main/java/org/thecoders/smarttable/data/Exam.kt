package org.thecoders.smarttable.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import java.util.*

/**
 * Created by Pascal on 27.03.2016.
 */

@Entity(tableName = "exam")
@TypeConverters(DateConverter::class)
class Exam(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val subject: String = "NULL",
        val topic: String = "NULL",
        val date: Date = Date(),
        val grade: String = "NULL"
)
