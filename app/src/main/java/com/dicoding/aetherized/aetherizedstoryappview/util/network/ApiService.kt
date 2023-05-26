package com.dicoding.aetherized.aetherizedstoryappview.util.network

import com.dicoding.aetherized.aetherizedstoryappview.data.model.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.model.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.response.GeneralResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.response.StoriesResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.response.StoryResponse
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
        @Part photo: MultipartBody.Part
    ): GeneralResponse

    @Multipart
    @POST("stories/guest")
    suspend fun addNewStoryGuest(
        @Part("description") description: RequestBody,
        @Part photo: MultipartBody.Part
    ): GeneralResponse

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): StoryResponse
}

