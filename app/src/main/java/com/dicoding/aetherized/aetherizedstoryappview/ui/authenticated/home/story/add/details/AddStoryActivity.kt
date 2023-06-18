package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dicoding.aetherized.aetherizedstoryappview.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityAddStoryBinding
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.HomeActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.camera.CameraActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.MyApplication
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.reduceFileImage
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.rotateImage
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.loopj.android.http.AsyncHttpClient
import java.io.File


class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var descriptionEditText: EditText
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var storyDescription: String = ""

    private val preferenceDataStore by lazy { (application as MyApplication).customPreference }

    private val viewModelFactory by lazy { ViewModelFactory(preferenceDataStore) }
    private val viewModel by viewModels<AddStoryViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }
    private lateinit var loginResult: LoginResult

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var imageFile: File? = null
    private var isGallery: Boolean? = false

    private val startCameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_REQUEST_CODE) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("imageFile", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("imageFile")
                } as? File
                isGallery = it.data?.getBooleanExtra("isGallery", false)
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

                myFile?.let { file ->
                    if (!isGallery!!) {
                        rotateImage(file, isBackCamera)
                    }
                    imageFile = file
                    binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
                }
            }
        }
    companion object {
        const val CAMERA_REQUEST_CODE = 2
    }

    private fun observeSettings() {
        settingsViewModel.loginResultLiveData.observe(this) { newLoginResult ->
            loginResult = newLoginResult ?: LoginResult()
            AsyncHttpClient.log.d("AddStoryActivityObserveSettings", "loginResult: $loginResult")

            binding.sendStoryButton.text = if (loginResult.token != "GUEST") "Add to Story" else "Add as Guest"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginResult = LoginResult()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        observeSettings()
        setupView()
        setupClickListeners()

        if (imageFile == null){
            openCamera()
        }

    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun setupClickListeners() {
        binding.openCamera.setOnClickListener {
            openCamera()
        }
        binding.addDescButton.setOnClickListener {
            showDescriptionInputDialog()
        }
        binding.sendStoryButton.setOnClickListener {
            if (imageFile == null) {
                Toast.makeText(
                    this,
                    "Please capture an image",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (storyDescription.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please add a description",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            uploadStory(storyDescription)
        }
        binding.locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                }
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        latitude = location?.latitude ?: 0.0
                        longitude = location?.longitude ?: 0.0
                    }
            } else {
                latitude = 0.0
                longitude = 0.0
            }
        }
    }


    private fun showDescriptionInputDialog() {
        descriptionEditText = EditText(this)
        descriptionEditText.inputType = InputType.TYPE_CLASS_TEXT
        descriptionEditText.setText(storyDescription)

        AlertDialog.Builder(this)
            .setTitle("Add Story Description")
            .setView(descriptionEditText)
            .setPositiveButton("OK") { _, _ ->
                storyDescription = descriptionEditText.text.toString()
            }
            .setNegativeButton("Cancel", null)
            .show()

    }

    private fun uploadStory(description: String) {
        if (imageFile != null) {
            val reducedImageFile = reduceFileImage(imageFile!!)

            viewModel.addNewStory(reducedImageFile, description, latitude, longitude, loginResult) { response ->
                if (!response.error) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun openCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        startCameraActivity.launch(intent)
    }

}

