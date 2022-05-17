package com.peanut.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.peanut.storyapp.data.local.datastore.UserPreferences
import com.peanut.storyapp.data.repository.UsersRepository
import com.peanut.storyapp.di.Injection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginViewModel(
    private val usersRepository: UsersRepository,
    private val userPreferences: UserPreferences
    ) : ViewModel(){
    fun loginUser(email:String, password:String) = usersRepository.login(email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferences.saveToken(token)
        }
    }

    class LoginModelFactory private constructor(
        private val userRepository: UsersRepository,
        private val userPreferences: UserPreferences
        ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepository, userPreferences) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: LoginModelFactory? = null

            fun getInstance(
                userPreferences: UserPreferences
            ): LoginModelFactory = instance ?: synchronized(this) {
                instance ?: LoginModelFactory(Injection.provideUserRepository(),userPreferences)
            }
        }
    }
}