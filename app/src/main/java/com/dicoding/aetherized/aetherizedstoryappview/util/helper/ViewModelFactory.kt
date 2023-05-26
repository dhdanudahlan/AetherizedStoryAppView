package com.dicoding.aetherized.aetherizedstoryappview.util.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.HomeViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.FeedsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.main.MainViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.login.LoginViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.register.RegisterViewModel

class ViewModelFactory(private val pref: CustomPreference) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref) as T
            }
            modelClass.isAssignableFrom(FeedsViewModel::class.java) -> {
                FeedsViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}