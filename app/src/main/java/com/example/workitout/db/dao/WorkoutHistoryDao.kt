package com.example.workitout.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.workitout.db.tables.WorkoutHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Upsert
    suspend fun upsert(workoutHistory: WorkoutHistoryEntity)

    @Query("SELECT * FROM WorkoutHistoryEntity ORDER BY date DESC")
    fun getAllEntries(): Flow<List<WorkoutHistoryEntity>>

    @Query("SELECT * FROM WorkoutHistoryEntity ORDER BY id DESC LIMIT 1")
    fun getLatestEntry(): Flow<WorkoutHistoryEntity>

}