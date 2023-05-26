package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityAddStoryBinding
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.camera.CameraActivity
import com.dicoding.aetherized.aetherizedstoryappview.util.network.ApiConfig
import java.io.File


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var descriptionEditText: EditText

    private var apiService = ApiConfig.getApiService()
    private var capturedImageFile: File? = null
    private var storyDescription: String = ""
    private lateinit var viewModel: AddStoryViewModel

    private val startCameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val capturedImageFilePath = result.data?.getStringExtra("capturedImageFile")
                capturedImageFile = capturedImageFilePath?.let { File(it) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupClickListeners()
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
//    private fun setupViewModel() {
//        viewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreference.getInstance(dataStore))
//        )[AddStoryViewModel::class.java]
//    }
    private fun setupClickListeners() {
        binding.openCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startCameraActivity.launch(intent)
        }
        binding.addDescButton.setOnClickListener {
            showDescriptionInputDialog()
        }
        binding.sendStoryButton.setOnClickListener {
            if (capturedImageFile == null || storyDescription.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please capture an image and add a description",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            uploadStory(capturedImageFile!!, storyDescription)
        }
    }


    private fun showDescriptionInputDialog() {
        descriptionEditText = EditText(this)
        descriptionEditText.inputType = InputType.TYPE_CLASS_TEXT

        AlertDialog.Builder(this)
            .setTitle("Add Story Description")
            .setView(descriptionEditText)
            .setPositiveButton("OK") { _, _ ->
                storyDescription = descriptionEditText.text.toString()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun uploadStory(imageFile: File, description: String) {
        viewModel.addNewStory(imageFile, description) { response ->
            if (!response.error) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CameraActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}