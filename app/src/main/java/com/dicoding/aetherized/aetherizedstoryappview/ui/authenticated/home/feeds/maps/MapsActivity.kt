package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.maps

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.data.repository.StoryRepository
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityMapsBinding
import com.dicoding.aetherized.aetherizedstoryappview.di.Injection
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import com.dicoding.aetherized.aetherizedstoryappview.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.MyApplication
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding
    private val preferenceDataStore by lazy { (application as MyApplication).customPreference }

    private val storyRepository: StoryRepository by lazy {
        val loginResult = LoginResult()
        if (loginResult != null) {
            Injection().provideRepository(this, loginResult)
        } else {
            throw IllegalStateException("LoginResult is not available.")
        }
    }

    private val viewModelFactory by lazy { ViewModelFactory(preferenceDataStore, storyRepository) }
    private val viewModel by viewModels<MapsViewModel> { viewModelFactory }



    private var storyList: List<Story> = emptyList()
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("MapsActivityFun", "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        storyList = viewModel.getLocalStoryList()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        Log.d("MapsActivityFun", "onMapReady")
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        if (storyList.isNotEmpty()) {
            Log.d("MapsActivityMarker", "storyList is NOT Empty")
            for (i in storyList) {
                if (!((i.lat).isNullOrEmpty() || (i.lon).isNullOrEmpty())){
                    Log.d("MapsActivityMarker", "Created: lat:${i.lat} lon:${i.lat}")
                    val storyMarker = LatLng(i.lat.toDouble(), i.lon.toDouble())
                    mMap.addMarker(MarkerOptions().position(storyMarker).title(i.name))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(storyMarker))
                }
            }
        } else {
            Log.d("MapsActivityMarker", "storyList is Empty")
        }
    }


}