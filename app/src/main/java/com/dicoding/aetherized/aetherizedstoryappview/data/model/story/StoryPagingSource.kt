package com.dicoding.aetherized.aetherizedstoryappview.data.model.story

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.ApiService

class StoryPagingSource(private val loginResult: LoginResult, private val apiService: ApiService, private val enableLocation: Boolean) :
PagingSource<Int, Story>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val token = "Bearer ${loginResult.token}"
            val position = params.key ?: INITIAL_PAGE_INDEX
            val location = if (enableLocation) 1 else 0
            Log.d("StoryPagingSource", "location: $location")
            val responseData = apiService.getAllStories(token, position, params.loadSize, location)
            val stories = responseData.data.map {
                it.toStoryEntity().toStory()
            }

            LoadResult.Page(
                data = stories,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (stories.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.d("StoryPagingSource", "Failed: $exception")
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        Log.d("StoryPagingSource", "getRefreshKey")
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}