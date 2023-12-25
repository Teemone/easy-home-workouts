package com.example.workitout.viewmodel

import android.content.Context
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.viewpager2.widget.ViewPager2
import com.example.workitout.data.AppPrefsDatastore
import com.example.workitout.data.Constants
import com.example.workitout.data.Exercises
import com.example.workitout.db.tables.WorkoutHistoryEntity
import com.example.workitout.db.tables.WorkoutInfoEntity
import com.example.workitout.db.dao.WorkoutHistoryDao
import com.example.workitout.db.dao.WorkoutInfoDao
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

const val TAG = "VIEW-MODEL DUMP"
const val EXERCISE = "exercise"

class CustomViewModel(
    private val workoutInfoDao: WorkoutInfoDao,
    private val workoutHistoryDao: WorkoutHistoryDao
): ViewModel() {
    private lateinit var datastore: AppPrefsDatastore

    private var _toolbar = MutableLiveData<Toolbar>()
    val toolbar: LiveData<Toolbar> = _toolbar

    private var _bottomNavView = MutableLiveData<BottomNavigationView>()
    val bottomNavView: LiveData<BottomNavigationView> = _bottomNavView

    private var _workoutInfoFlow = MutableStateFlow<List<WorkoutInfoEntity>>(listOf())
    val workoutInfoFlow: StateFlow<List<WorkoutInfoEntity>> = _workoutInfoFlow

    private var _workoutHistoryFlow = MutableStateFlow<List<WorkoutHistoryEntity>>(listOf())
    val workoutHistoryFlow: StateFlow<List<WorkoutHistoryEntity>> = _workoutHistoryFlow

    private var _getFollowSysDefFlow = MutableStateFlow(true)
    val getFollowSysDefFlow = _getFollowSysDefFlow.asStateFlow()

    private var _exerciseIsCompletedFlow = MutableStateFlow(false)
    val exerciseIsCompletedFlow: StateFlow<Boolean> = _exerciseIsCompletedFlow

    val exercises = Constants.returnExercises()

    private val _countdownState = MutableStateFlow(TimerState.RUNNING)
    val countdownState: StateFlow<TimerState> get() = _countdownState


    init {
        getAllWorkoutInfo()
        getAllWorkoutHistory()
    }



    /*
     START
     OF
     DB-QUERIES
      */

    private fun getAllWorkoutInfo(){
        viewModelScope.launch {
            try {
                workoutInfoDao.getAllEntries().collect{
                    _workoutInfoFlow.value = it
                }
            }catch (e: Exception){
                e.printStackTrace()
            }


        }
    }

    fun insertHistory(historyItem: WorkoutHistoryEntity){
        viewModelScope.launch {
            try {
                workoutHistoryDao.insert(historyItem)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun getAllWorkoutHistory(){
        viewModelScope.launch {
            workoutHistoryDao.getAllEntries().collect{
                _workoutHistoryFlow.value = it
            }
        }
    }

    fun insertAllEntries(){
        viewModelScope.launch(Dispatchers.IO) {
            workoutInfoDao.insertAll(
                listOf(
                    WorkoutInfoEntity(name = "Lunges", description = "Lunges are a lower body exercise where you step forward and lower your body until your front and back knees form 90-degree angles.", muscles = "Quadriceps, Hamstrings, Glutes"),
                    WorkoutInfoEntity(name = "Pushups", description = "Pushups are a compound exercise that targets multiple muscles in the upper body. From a plank position, you lower your body by bending your elbows and then push back up.", muscles = "Chest, Shoulders, Triceps, Core", duration = 20000),
                    WorkoutInfoEntity(name = "Squats", description = "Squats are a lower body exercise where you lower your body by bending your knees and hips and then return to a standing position.", muscles = "Quadriceps, Hamstrings, Glutes, Core"),
                    WorkoutInfoEntity(name = "Side Planks", description = "Side planks are a core exercise where you balance on one forearm and the side of your foot while keeping your body in a straight line.", muscles = "Obliques, Transverse Abdominis", duration = 20000),
                    WorkoutInfoEntity(name = "Planks", description = "Planks are a core exercise where you support your body on your forearms and toes, maintaining a straight line from head to feet.", muscles = "Rectus Abdominis, Transverse Abdominis, Obliques"),
                    WorkoutInfoEntity(name = "Glute Bridge", description = "Glute bridge is a lower body exercise where you lie on your back, bend your knees, and lift your hips off the ground, squeezing your glutes at the top.", muscles = "Glutes, Hamstrings", duration = 40000),
                    WorkoutInfoEntity(name = "Jumping Jacks", description = "Jumping jacks are a cardiovascular exercise where you start with your feet together and hands at your sides, then jump while spreading your legs and raising your arms overhead.", muscles = "Full body, Cardiovascular", duration = 45000)
                )
            )
        }
    }

    /*
    END
    OF
    DB-QUERIES
     */



    /*
    START
    OF
    GETTERS
     */

    fun getExerciseProgress(context: Context): LiveData<Double> {
        datastore = AppPrefsDatastore(context)
        return datastore.getExerciseProgress.asLiveData()

    }

    fun getExerciseIsCompleted(context: Context): LiveData<Boolean?> {
        datastore = AppPrefsDatastore(context)
        return datastore.getExerciseIsCompleted.asLiveData()

    }

    fun getWorkoutInfoAddedToDb(context: Context): LiveData<Boolean> {
        datastore = AppPrefsDatastore(context)
        return datastore.getWorkoutInfoAddedToDb.asLiveData()

    }

    fun getCurrentExerciseId(context: Context): LiveData<Int> {
        datastore = AppPrefsDatastore(context)
        return datastore.getCurrentExerciseId.asLiveData()
    }

    fun getIsNightMode(context: Context): Flow<Boolean?>{
        datastore = AppPrefsDatastore(context)
        return datastore.getIsNightMode
    }

    fun getFollowsSysDef(context: Context): Flow<Boolean?>{
        datastore = AppPrefsDatastore(context)
        return datastore.getFollowsSysDef
    }

    fun getCountdown(timeMillis: Long): Flow<Int> {
        var tSecs = (timeMillis/1000).toInt()
        _countdownState.value = TimerState.RUNNING

        return flow {
            emit(tSecs)

            while (tSecs > 0){
                delay(1000L)
                tSecs--
                emit(tSecs)
            }
            _countdownState.value = TimerState.FINISHED
        }
    }

    /*
    END
    OF
    GETTERS
     */




    /*
    START
    OF
    SETTERS
     */

    fun setToolbar(tb: Toolbar){
        _toolbar.value = tb
    }

    fun setBnv(bnv: BottomNavigationView){
        _bottomNavView.value = bnv
    }

    fun setTimeLeftForCurrentWorkout(timeLeft: Int, context: Context, exercise: Exercises){
        datastore = AppPrefsDatastore(context)
        val progress = progress(exercise, timeLeft)
        val exerciseIsCompleted = progress.equals(100.0)

        viewModelScope.launch {
            datastore.apply {
                setTimeLeftForCurrentWorkout(timeLeft)
                setExerciseProgress(progress)
                setExerciseIsCompleted(exerciseIsCompleted)
            }
            setExerciseIsCompletedFlow(exerciseIsCompleted)
        }
    }

    private fun setExerciseIsCompletedFlow(isCompleted: Boolean){
        _exerciseIsCompletedFlow.value = isCompleted
    }

    fun setFollowSysDefFlow(followSysDef: Boolean){
        _getFollowSysDefFlow.value = followSysDef
    }

    fun setCurrentExerciseId(context: Context, id: Int){
        datastore = AppPrefsDatastore(context)

        viewModelScope.launch {
            datastore.setCurrentExerciseId(id)
        }
    }

    fun setWorkoutInfoAddedToDb(context: Context, isAdded: Boolean){
        datastore = AppPrefsDatastore(context)

        viewModelScope.launch {
            datastore.setWorkoutInfoAddedToDb(isAdded)
        }
    }

    fun setIsNightMode(context: Context, isNightMode: Boolean){
        datastore = AppPrefsDatastore(context)

        viewModelScope.launch (Dispatchers.Default){
            datastore.setIsNightMode(isNightMode)
        }
    }

    fun setFollowsSysDef(context: Context, followsSysDef: Boolean){
        datastore = AppPrefsDatastore(context)

        viewModelScope.launch (Dispatchers.Default) {
            datastore.setFollowsSysDef(followsSysDef)
        }
    }

    /*
    START
    OF
    SETTERS
     */


    private fun progress(exercises: Exercises, timeLeft: Int): Double{
        val duration = exercises.durationAsInt()
        return (1 - timeLeft.toDouble()/duration.toDouble()) * 100
    }

    fun appendZero(value: Int): String{
        if (value<10)
            return "0$value"
        return value.toString()
    }

    enum class TimerState {
        RUNNING,
        PAUSED,
        FINISHED
    }

}

@Suppress("UNCHECKED_CAST")
class CustomViewModelFactory(
    private val workoutInfoDao: WorkoutInfoDao,
    private val workoutHistoryDao: WorkoutHistoryDao
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when (modelClass) {
            CustomViewModel::class.java -> {
                // Create VM
                CustomViewModel(workoutInfoDao, workoutHistoryDao)
            }
            else -> throw IllegalArgumentException("Unknown class $modelClass")
        } as T
    }
}
