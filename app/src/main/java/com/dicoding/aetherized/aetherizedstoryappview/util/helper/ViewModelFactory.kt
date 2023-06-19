package com.dicoding.aetherized.aetherizedstoryappview.util.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.HomeViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.FeedsPagingViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.details.DetailsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.maps.MapsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details.AddStoryViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.login.LoginViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.register.RegisterViewModel

class ViewModelFactory(private val preferenceDataStore: CustomPreference? = null, private val storyRepository: StoryRepository? = null) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(preferenceDataStore!!) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preferenceDataStore!!) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(preferenceDataStore!!) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(preferenceDataStore!!) as T
            }
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> {
                DetailsViewModel(preferenceDataStore!!) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(preferenceDataStore!!) as T
            }
            modelClass.isAssignableFrom(FeedsPagingViewModel::class.java) -> {
                FeedsPagingViewModel(storyRepository!!) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}