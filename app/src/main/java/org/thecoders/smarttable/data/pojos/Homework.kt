package org.thecoders.smarttable.data.pojos

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import org.thecoders.smarttable.data.DateConverter
import java.util.*

/**
 * Created by Pascal on 25.03.2016.
 * Object that contains all important characteristics of a homework
 */

@Entity(tableName = "homework")
@TypeConverters(DateConverter::class)
class Homework(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val subject: String = "NULL",
        val task: String = "NULL",
        val date_start: Date = Date(),
        val date_deadline: Date = Date(),
        val finished: Boolean = false,
        val effort: String = "NULL"
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("ID: $id").append("\n")
        sb.append("Subject: $subject").append("\n")
        sb.append("Task: $task").append("\n")
        sb.append("Startdate: $date_start").append("\n")
        sb.append("Deadline: $date_deadline").append("\n")
        sb.append("Finished: $finished").append("\n")
        sb.append("Effort: $effort").append("\n")
        return sb.toString()
    }
}
