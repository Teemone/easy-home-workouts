package com.example.workitout.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workitout.data.AppPrefsDatastore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel: ViewModel() {
    private lateinit var datastore: AppPrefsDatastore

    private val _getIsOnboardingCompletedFlow = MutableStateFlow<Boolean?>(null)
    val getIsOnboardingCompletedFlow: StateFlow<Boolean?> = _getIsOnboardingCompletedFlow



    fun getIsOnboardingCompleted(context: Context): Flow<Boolean?> {
        datastore = AppPrefsDatastore(context)
        return datastore.getIsOnboardingCompleted
    }

    fun setIsOnboardingCompleted(context: Context, isCompleted: Boolean){
        datastore = AppPrefsDatastore(context)

        viewModelScope.launch (Dispatchers.Default) {
            datastore.setIsOnboardingCompleted(isCompleted)
        }
    }

}