package com.dicoding.aetherized.aetherizedstoryappview.remote

import androidx.paging.*
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.aetherized.aetherizedstoryappview.DataDummy
import com.dicoding.aetherized.aetherizedstoryappview.data.local.database.StoryDatabase
import com.dicoding.aetherized.aetherizedstoryappview.data.local.entity.StoryEntity
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.StoryRemoteMediator
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.api.ApiService
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.GeneralResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.StoriesResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.StoryResponse
import com.dicoding.aetherized.aetherizedstoryappview.data.remote.response.UserResponse
import com.dicoding.aetherized.aetherizedstoryappview.model.user.LoginResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            loginResult = LoginResult(),
            database = mockDb,
            apiService = mockApi,
            enableLocation = true
        )
        val pagingState = PagingState<Int, StoryEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}


class FakeApiService : ApiService {
    override suspend fun addNewStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody,
        lon: RequestBody
    ): GeneralResponse {
        TODO("Not yet implemented")
    }

    override suspend fun register(requestBody: Map<String, String>): GeneralResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(requestBody: Map<String, String>): UserResponse<LoginResult> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewStoryGuest(
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: RequestBody,
        lon: RequestBody
    ): GeneralResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAllStories(
        token: String,
        page: Int,
        size: Int,
        location: Int
    ): StoriesResponse {
        return StoriesResponse(error = false, message = "Success", DataDummy.generateDummyStoryResponse(100).map { it.toStoryDto() })
    }

    override suspend fun getStoryDetail(token: String, id: String): StoryResponse {
        TODO("Not yet implemented")
    }
}