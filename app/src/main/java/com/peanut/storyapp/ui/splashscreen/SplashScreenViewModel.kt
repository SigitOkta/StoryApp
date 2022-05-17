package com.peanut.storyapp.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.peanut.storyapp.data.local.datastore.UserPreferences

class SplashScreenViewModel(private val userPreferences: UserPreferences) : ViewModel() {
    fun getToken(): LiveData<String>{
        return userPreferences.getToken().asLiveData()
    }

    class SplashModelFactory private constructor(
        private val userPreferences: UserPreferences
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) {
                return SplashScreenViewModel(userPreferences) as T }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
        companion object {
            @Volatile
            private var instance: SplashModelFactory? = null
            fun getInstance(
                userPreferences: UserPreferences
            ): SplashModelFactory = instance ?: synchronized(this) {
                instance ?: SplashModelFactory(
                    userPreferences
                )
            }.also { instance = it }
        }
    }
}