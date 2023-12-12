package com.example.workitout.db.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkoutHistoryEntity (
    @ColumnInfo @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    @ColumnInfo
    val date: String,
    @ColumnInfo
    val workoutName: String,
    @ColumnInfo
    val status: String,
    @ColumnInfo
    val progress: Double
)