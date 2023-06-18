package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.aetherized.aetherizedstoryappview.MainCoroutineRule
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.utils.DataDummy
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FeedsPagingViewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

//    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyPagingViewModel: FeedsPagingViewModel
    private val dummyStoryList = DataDummy.generateStoryNewsEntity(10)


    @Before
    fun setUp() {
        storyRepository = Mockito.mock(StoryRepository::class.java)
        storyPagingViewModel = FeedsPagingViewModel(storyRepository)
    }

    @Test
    fun `when getAllStories Should Not Be Null and Return Success`()  = mainCoroutineRule.runBlockingTest {
        val expectedStories = MutableLiveData<PagingData<Story>>().cachedIn(storyPagingViewModel.viewModelScope)
        `when`(storyRepository.getAllStories(true)).thenReturn(expectedStories)
        val actualStories = storyPagingViewModel.getStories(true)

        `when`(storyPagingViewModel.getStories(true)).thenReturn(actualStories)
        Assert.assertNotNull(actualStories)
    }
}