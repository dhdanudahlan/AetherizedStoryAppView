package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityHomeBinding
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.FeedsFragment
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.profile.ProfileFragment
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.StoryFragment
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.camera.CameraActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details.AddStoryActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.main.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val viewModelFactory by lazy { ViewModelFactory(CustomPreference(this)) }
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }

    private lateinit var toolBar: Toolbar
    private lateinit var toolbarTitle1: String
    private lateinit var toolbarTitle2: String
    private lateinit var toolbarTitle3: String
    private lateinit var toolbarTitleDetails: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeValue()
        setupView()
        observeSettings()

    }
    private fun initializeValue(){
        toolbarTitle1 = resources.getString(R.string.toolBarTitle1)
        toolbarTitle2 = resources.getString(R.string.toolBarTitle2)
        toolbarTitle3 = resources.getString(R.string.toolBarTitle3)
        toolbarTitleDetails = "Story Details"


    }

    private fun observeSettings() {

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        settingsViewModel.getHomePageSettings().observe(this) { homepage ->
            bottomNav(homepage)
        }
    }
    private fun setupView(){
        toolBar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)

    }

    private fun bottomNav(homepage: String) {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        when (homepage) {
            "Feeds" -> {
                val fragment = FeedsFragment()
                replaceFragment(fragment)
                replaceToobar(toolbarTitle1)
                bottomNavigation.selectedItemId = R.id.feeds
            }
            "Add" -> {

                val fragment = StoryFragment()
                replaceFragment(fragment)
                replaceToobar(toolbarTitle2)

                bottomNavigation.selectedItemId = R.id.addstory
            }
            "Profile" -> {
                val fragment = ProfileFragment()
                replaceFragment(fragment)
                replaceToobar(toolbarTitle3)
                bottomNavigation.selectedItemId = R.id.profile
            }
            else -> {
                val fragment = FeedsFragment()
                replaceFragment(fragment)
                replaceToobar(toolbarTitle1)
                bottomNavigation.selectedItemId = R.id.feeds
            }
        }

        bottomNavigation.setOnItemSelectedListener  { item ->
            val fragment = when(item.itemId) {
                R.id.feeds -> FeedsFragment()
                R.id.addstory -> StoryFragment()
                R.id.profile -> ProfileFragment()
                else -> FeedsFragment()
            }
            when(item.itemId) {
                R.id.feeds -> replaceToobar(toolbarTitle1)
                R.id.addstory -> replaceToobar(toolbarTitle2)
                R.id.profile -> replaceToobar(toolbarTitle3)
                else -> replaceToobar(toolbarTitle1)
            }
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if(currentFragment?.javaClass != fragment.javaClass) {
                replaceFragment(fragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
    private fun replaceToobar(title: String) {
        toolBar.title= title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.action_logout -> {
                settingsViewModel.logout()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}