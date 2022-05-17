package com.peanut.storyapp.di


import com.peanut.storyapp.data.remote.retrofit.ApiConfig
import com.peanut.storyapp.data.repository.UsersRepository

object Injection {
    fun provideUserRepository(): UsersRepository {
        val apiService = ApiConfig.getApiService()
        return UsersRepository.getInstance(apiService)
    }
}