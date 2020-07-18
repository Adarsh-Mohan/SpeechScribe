package com.example.speechscribe.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Created by Adarsh Mohan on 18/07/2020.
 * Email : valsalamohankallerIl@gmail.com
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "text")
    var noteText: String,
    @ColumnInfo(name = "date")
    var noteDate: Long
) {
    @Ignore
    var checked: Boolean = false
}