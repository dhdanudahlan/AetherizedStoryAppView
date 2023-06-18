package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings

import androidx.lifecycle.*
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.data.model.user.LoginResult
import kotlinx.coroutines.launch

class SettingsViewModel (private val preferenceDataStore: CustomPreference) : ViewModel() {
    private val _prefDarkMode = MutableLiveData<Boolean>()
    val prefDarkMode: LiveData<Boolean> get() = _prefDarkMode

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> get() = _loginResult


    val loginResultLiveData = preferenceDataStore.loginResultFlow.asLiveData()


    fun getThemeSettings(): LiveData<Boolean> {
        return preferenceDataStore.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            preferenceDataStore.saveThemeSetting(isDarkMode)
        }
    }

    fun getHomePageSettings(): LiveData<String> {
        return preferenceDataStore.getHomePageSetting().asLiveData()
    }

    fun saveHomePageSetting(homepage: String) {
        viewModelScope.launch {
            preferenceDataStore.saveHomePageSetting(homepage)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceDataStore.clearLogin()
        }
    }
}