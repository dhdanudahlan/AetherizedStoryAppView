package com.dicoding.aetherized.aetherizedstoryappview.util.helper

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.aetherized.aetherizedstoryappview.data.model.LoginResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")

class CustomPreference(private val context: Context) {
    private val gson = Gson()

    private val loginResultKey = stringPreferencesKey(Constants.PREF_LOGIN)
    private val themeKey = booleanPreferencesKey(Constants.PREFS_KEY_THEME)
    private val homePageKey = stringPreferencesKey(Constants.PREFS_KEY_HOMEPAGE)

    private val _prefDarkMode = MutableLiveData<Boolean>()
    val prefDarkMode: LiveData<Boolean> get() = _prefDarkMode

    val loginResultFlow: Flow<LoginResult?> = context.dataStore.data.map { preferences ->
        val loginResultJson = preferences[loginResultKey]
        if (loginResultJson != null) {
            gson.fromJson(loginResultJson, LoginResult::class.java)
        } else {
            null
        }
    }

    suspend fun saveLogin(loginResult: LoginResult) {
        val loginResultJson = gson.toJson(loginResult)
        context.dataStore.edit { preferences ->
            preferences[loginResultKey] = loginResultJson
        }
    }

    fun getThemeSetting(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    fun getHomePageSetting(): Flow<String> {
        return context.dataStore.data.map { preferences ->
            preferences[homePageKey] ?: "Feeds"
        }
    }

    suspend fun saveHomePageSetting(homepage: String) {
        context.dataStore.edit { preferences ->
            preferences[homePageKey] = homepage
        }
    }


//    companion object {
//        @Volatile
//        private var INSTANCE: UserPreference? = null
//
//        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
//            return INSTANCE ?: synchronized(this) {
//                val instance = UserPreference(dataStore)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}