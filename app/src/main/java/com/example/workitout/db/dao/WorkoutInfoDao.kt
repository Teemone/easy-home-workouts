package com.example.workitout.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workitout.db.tables.WorkoutInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutInfoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(workoutInfo: List<WorkoutInfoEntity>)

    @Query("SELECT * FROM WorkoutInfoEntity")
    fun getAllEntries(): Flow<List<WorkoutInfoEntity>>

}