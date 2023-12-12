package com.example.workitout.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workitout.data.Constants

@Entity
data class WorkoutInfoEntity (
    @ColumnInfo @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val description: String,
    @ColumnInfo
    val muscles: String,
    @ColumnInfo
    val duration: Long = Constants.DEFAULT_DURATION
)