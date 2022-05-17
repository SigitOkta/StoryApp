package com.peanut.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.peanut.storyapp.data.Result
import com.peanut.storyapp.data.remote.response.LoginResponse
import com.peanut.storyapp.data.remote.response.RegisterResponse
import com.peanut.storyapp.data.remote.retrofit.ApiService
import okio.IOException
import retrofit2.HttpException

class UsersRepository private constructor(
    private val apiService: ApiService
){
    fun register(name: String,email: String, password: String):LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.registerUser(name,email,password)
            Log.d("register", "response: $response ")
            if(!response.error){
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message))
                Log.d("register", "errorelse: ${response.message} ")
            }

        }catch (e: Exception) {
            Log.d("register", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }catch (e: HttpException) {
            Log.d("register", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        } catch (e: IOException) {
            Log.d("register", "error: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }
    fun login(email: String, password: String):LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.loginUser(email, password)
            emit(Result.Success(response))
        }catch (e: Exception) {
            Log.d("login", "error: ${e.message.toString()} ")

        }
    }

    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService)
            }.also { instance = it }
    }
}