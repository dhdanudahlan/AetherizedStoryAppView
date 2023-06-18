package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class FeedsPagingViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var observer: Observer<PagingData<Story>>

    private lateinit var viewModel: FeedsPagingViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = FeedsPagingViewModel(storyRepository)
    }

    @Test
    fun `getStories returns non-null data`() = runBlockingTest {
        val fakeData = PagingData.from(listOf(Story("story-1", "Test Story", "This is a test story", "https://example.com/images/1.png", "2023-06-18", "0", "0")))
        Mockito.`when`(storyRepository.getAllStories(true)).thenReturn(flowOf(fakeData).asLiveData())

        viewModel.getStories(true).observeForever(observer)
        Mockito.verify(observer).onChanged(fakeData)
    }

    @Test
    fun `getStories returns null data`() = runBlockingTest {
        val fakeData = PagingData.empty<Story>()
        Mockito.`when`(storyRepository.getAllStories(true)).thenReturn(flowOf(fakeData).asLiveData())

        viewModel.getStories(true).observeForever(observer)
        Mockito.verify(observer).onChanged(fakeData)
    }

}