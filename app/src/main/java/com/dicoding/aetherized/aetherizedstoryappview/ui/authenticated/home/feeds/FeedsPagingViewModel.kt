package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.aetherized.aetherizedstoryappview.data.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository

class FeedsPagingViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStories(enableLocation: Boolean) =
        storyRepository.getAllStories(enableLocation).cachedIn(viewModelScope)
}

