package com.dicoding.aetherized.aetherizedstoryappview.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.dicoding.aetherized.aetherizedstoryappview.data.local.database.StoryDatabase
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.api.ApiService
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.StoryRemoteMediator

class StoryRepository(private val loginResult: LoginResult, private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getAllStories(enableLocation: Boolean): LiveData<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(loginResult, storyDatabase, apiService, enableLocation),
            pagingSourceFactory = {
                storyDatabase.storyDao.getAllStories()
            }
        ).liveData.map { pagingData ->
            pagingData.map { storyEntity ->
                storyEntity.toStory()
            }
        }
    }
}