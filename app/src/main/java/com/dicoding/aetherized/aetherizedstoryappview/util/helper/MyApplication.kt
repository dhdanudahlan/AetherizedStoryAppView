package com.dicoding.aetherized.aetherizedstoryappview.util.helper

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

class MyApplication : Application() {
    private val dataStore: DataStore<Preferences> by preferencesDataStore(name = "preference_datastore")
    lateinit var customPreference: CustomPreference

    override fun onCreate() {
        super.onCreate()
        customPreference = CustomPreference(dataStore)
    }
}