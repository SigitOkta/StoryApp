package com.peanut.storyapp.ui.main

import android.content.Context
import androidx.lifecycle.*
import com.peanut.storyapp.data.local.datastore.UserPreferences
import com.peanut.storyapp.di.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    fun getToken(): LiveData<String>{
        return userPreferences.getToken().asLiveData()
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO){
            userPreferences.deleteToken()
        }
    }

    class MainModelFactory private constructor(
        private val userPreferences: UserPreferences
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(userPreferences) as T }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: MainModelFactory? = null

            fun getInstance(
                userPreferences: UserPreferences
            ): MainModelFactory = instance ?: synchronized(this) {
                instance ?: MainModelFactory(
                    userPreferences
                )
            }.also { instance = it }
        }
    }
}