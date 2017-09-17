package org.thecoders.smarttable.data.pojos

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "subjects")
class Subject(
        @PrimaryKey(autoGenerate = true) val id: Long,
        val name: String = "NULL",
        val teacher: String = "NULL",
        val category: String = "NULL",
        val color: Int = 0
)
