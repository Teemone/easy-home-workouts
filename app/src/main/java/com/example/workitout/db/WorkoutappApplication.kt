package com.example.workitout.db

import android.app.Application

class WorkoutappApplication: Application() {
    val database: WorkoutAppDatabase by lazy {
        WorkoutAppDatabase.getInstance(this)
    }
}