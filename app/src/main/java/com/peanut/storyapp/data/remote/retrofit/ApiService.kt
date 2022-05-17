package com.peanut.storyapp.data.remote.retrofit

import com.peanut.storyapp.data.remote.response.AddStoryResponse
import com.peanut.storyapp.data.remote.response.LoginResponse
import com.peanut.storyapp.data.remote.response.RegisterResponse
import com.peanut.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //POST Login
    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    //POST Register
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    //POST Story
    @Multipart
    @POST("stories")
    fun addStory(
        @Header("Authorization") token: String,
        @Part ("description") des: String,
        @Part file: MultipartBody.Part
    ) : Call<AddStoryResponse>

    //GET Stories
    @GET("stories")
    fun getListStories(
        @Header("Authorization") token: String
    ): Call<StoriesResponse>
}