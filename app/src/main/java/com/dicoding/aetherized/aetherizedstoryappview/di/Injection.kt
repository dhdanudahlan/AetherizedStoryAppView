package com.dicoding.aetherized.aetherizedstoryappview.di

import android.content.Context
import com.dicoding.aetherized.aetherizedstoryappview.data.local.database.StoryDatabase
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig

class Injection {
    fun provideRepository(context: Context, loginResult: LoginResult): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(loginResult, database, apiService)
    }
}