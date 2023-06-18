package com.dicoding.aetherized.aetherizedstoryappview.data.remote

import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.GeneralResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.StoriesResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    suspend fun register(
        @Body requestBody: Map<String, String>
    ): GeneralResponse

    @POST("login")
    suspend fun login(
        @Body requestBody: Map<String, String>
    ): UserResponse<LoginResult>

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Header("Authorization") token: String,
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): GeneralResponse

    @Multipart
    @POST("stories/guest")
    suspend fun addNewStoryGuest(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): GeneralResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int,
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): StoryResponse
}

