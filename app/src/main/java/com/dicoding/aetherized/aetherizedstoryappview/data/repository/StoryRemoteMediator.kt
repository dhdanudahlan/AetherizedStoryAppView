package com.dicoding.aetherized.aetherizedstoryappview.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.aetherized.aetherizedstoryappview.data.local.database.RemoteKeys
import com.dicoding.aetherized.aetherizedstoryappview.data.local.database.StoryDatabase
import com.dicoding.aetherized.aetherizedstoryappview.data.local.entity.StoryEntity
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val loginResult: LoginResult,
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val enableLocation: Boolean
)  : RemoteMediator<Int, StoryEntity>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val token = "Bearer ${loginResult.token}"
            val location = if (enableLocation) 1 else 0

            val responseData =
                apiService.getAllStories(token, page, state.config.pageSize, location)

            val storyEntityList = responseData.data.map { it.toStoryEntity() }
            val storyList = storyEntityList.map { it.toStory() }

            val endOfPaginationReached = storyList.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao.deleteRemoteKeys()
                    database.storyDao.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.data.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao.insertAll(keys)
                database.storyDao.insertStories(storyEntityList)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            Log.d("StoryRemoteMediator", "database.withTransaction $exception")
            return MediatorResult.Error(exception)
        }
    }
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao.getRemoteKeysId(data.toStory().id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao.getRemoteKeysId(data.toStory().id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.toStory()?.id?.let { id ->
                database.remoteKeysDao.getRemoteKeysId(id)
            }
        }
    }
}