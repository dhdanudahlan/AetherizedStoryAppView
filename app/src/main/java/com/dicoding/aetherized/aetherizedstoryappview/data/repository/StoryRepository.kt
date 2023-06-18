package com.dicoding.aetherized.aetherizedstoryappview.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.liveData
import com.dicoding.aetherized.aetherizedstoryappview.data.local.database.StoryDatabase
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.StoryPagingSource
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiService

class StoryRepository(private val loginResult: LoginResult, private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getAllStories(enableLocation: Boolean): LiveData<PagingData<Story>> {
        Log.d("StoryRepository", "getRefreshKey")
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(loginResult, apiService, enableLocation)
            }
        ).liveData
    }
}