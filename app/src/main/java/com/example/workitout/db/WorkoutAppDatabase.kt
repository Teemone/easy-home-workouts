package com.example.workitout.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.workitout.db.dao.WorkoutHistoryDao
import com.example.workitout.db.dao.WorkoutInfoDao
import com.example.workitout.db.tables.WorkoutHistoryEntity
import com.example.workitout.db.tables.WorkoutInfoEntity

const val LATEST_VERSION = 1

@Database(
    entities = [
        WorkoutInfoEntity::class,
        WorkoutHistoryEntity::class
               ],
    version = LATEST_VERSION,
)
abstract class WorkoutAppDatabase : RoomDatabase(){

    abstract fun workoutInfoDao(): WorkoutInfoDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: WorkoutAppDatabase? = null

        fun getInstance(context: Context): WorkoutAppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    WorkoutAppDatabase::class.java,
                    "workoutapp_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}