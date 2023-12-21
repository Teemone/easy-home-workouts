package com.example.workitout.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private const val PREFS_DATASTORE = "app_prefs_datastore"
private val Context.datastore: DataStore<Preferences> by
preferencesDataStore(name = PREFS_DATASTORE)

class AppPrefsDatastore (private val context: Context) {
    private val timeLeftForCurrentWorkout = intPreferencesKey("timeLeftForCurrentWorkout")
    val getTimeLeftForCurrentWorkout = context.datastore.data.map { prefs -> prefs[timeLeftForCurrentWorkout] ?: 0 }

    private val exerciseProgress = doublePreferencesKey("exerciseProgress")
    val getExerciseProgress = context.datastore.data.map { prefs -> prefs[exerciseProgress] ?: 0.0 }

    // Remove
    private val completedExercisesId = stringSetPreferencesKey("completedExercisesId")
    val getCompletedExercisesId = context.datastore.data.map { prefs -> prefs[completedExercisesId] ?: setOf() }

    private val exerciseIsCompleted = booleanPreferencesKey("exerciseIsCompleted")
    val getExerciseIsCompleted = context.datastore.data.map { prefs -> prefs[exerciseIsCompleted] }

    private val isNightMode = booleanPreferencesKey("isNightMode")
    val getIsNightMode = context.datastore.data.map { prefs -> prefs[isNightMode] }

    private val currentExerciseId = intPreferencesKey("currentExerciseId")
    val getCurrentExerciseId = context.datastore.data.map { prefs -> prefs[currentExerciseId] ?: 0 }

    private val workoutInfoAddedToDb = booleanPreferencesKey("workoutInfoAddedToDb")
    val getWorkoutInfoAddedToDb = context.datastore.data.map { prefs -> prefs[workoutInfoAddedToDb] ?: false }





    suspend fun setTimeLeftForCurrentWorkout(timeLeft: Int) =
        context.datastore.edit {
            prefs ->
            prefs[timeLeftForCurrentWorkout] = timeLeft
        }

    suspend fun setExerciseProgress(progress: Double) =
        context.datastore.edit {
                prefs ->
            prefs[exerciseProgress] = progress
        }

    suspend fun setExerciseIsCompleted(isCompleted: Boolean) =
        context.datastore.edit {
                prefs ->
            prefs[exerciseIsCompleted] = isCompleted
        }

    suspend fun setWorkoutInfoAddedToDb(isAdded: Boolean) =
        context.datastore.edit {
                prefs ->
            prefs[workoutInfoAddedToDb] = isAdded
        }

    suspend fun setCurrentExerciseId(id: Int) =
        context.datastore.edit {
                prefs ->
            prefs[currentExerciseId] = id
        }

    suspend fun setIsNightMode(isNight: Boolean) =
        context.datastore.edit {
                prefs ->
            prefs[isNightMode] = isNight
        }
}