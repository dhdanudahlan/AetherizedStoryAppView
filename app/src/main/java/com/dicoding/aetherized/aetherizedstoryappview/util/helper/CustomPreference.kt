package com.dicoding.aetherized.aetherizedstoryappview.util.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.aetherized.aetherizedstoryappview.model.user.LoginResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CustomPreference(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val userId = stringPreferencesKey("user_id")
        val name = stringPreferencesKey("name")
        val token = stringPreferencesKey("token")
    }

    val loginResultFlow: Flow<LoginResult> = dataStore.data.map { preferences ->
        LoginResult(
            preferences[PreferencesKeys.userId] ?: "GUEST",
            preferences[PreferencesKeys.name] ?: "GUEST",
            preferences[PreferencesKeys.token] ?: "GUEST"
        )
    }

    suspend fun saveLogin(loginResult: LoginResult) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.userId] = loginResult.userId
            preferences[PreferencesKeys.name] = loginResult.name
            preferences[PreferencesKeys.token] = loginResult.token
        }
    }

    suspend fun clearLogin() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.userId)
            preferences.remove(PreferencesKeys.name)
            preferences.remove(PreferencesKeys.token)
        }
    }

    private val themeKey = booleanPreferencesKey(Constants.PREFS_KEY_THEME)
    private val homePageKey = stringPreferencesKey(Constants.PREFS_KEY_HOMEPAGE)

    private val _prefDarkMode = MutableLiveData<Boolean>()
    val prefDarkMode: LiveData<Boolean> get() = _prefDarkMode

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    fun getHomePageSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[homePageKey] ?: "Feeds"
        }
    }

    suspend fun saveHomePageSetting(homepage: String) {
        dataStore.edit { preferences ->
            preferences[homePageKey] = homepage
        }
    }
}