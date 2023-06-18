package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.feeds.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dicoding.aetherized.aetherizedstoryappview.R
import com.dicoding.aetherized.aetherizedstoryappview.model.story.Story
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    private var storyList: ArrayList<Story> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

        Log.d("MapsActivityFun", "onCreate")
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent != null) {
            Log.d("MapsActivity", "Intent is NOT null")
            @Suppress("DEPRECATION")
            storyList = (intent.getSerializableExtra("storyList") as? ArrayList<Story>)!!
            Log.d("MapsActivity", "Intent is NOT null ${storyList.size}")
        } else {
            Log.d("MapsActivity", "Intent is null ${storyList.size}")
        }

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