package com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.details

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.aetherized.aetherizedstoryappview.data.model.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityAddStoryBinding
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.story.add.camera.CameraActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.rotateFile
import java.io.File


class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var descriptionEditText: EditText

    private var storyDescription: String = ""

    private val viewModelFactory by lazy { ViewModelFactory(CustomPreference(this)) }
    private val viewModel by viewModels<AddStoryViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }
    private lateinit var loginResult: LoginResult


    private var capturedImageFile: File? = null

    private val startCameraActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == CAMERA_REQUEST_CODE) {
                val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.data?.getSerializableExtra("capturedImageFile", File::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    it.data?.getSerializableExtra("capturedImageFile")
                } as? File

                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

                myFile?.let { file ->
                    rotateFile(file, isBackCamera)
                    capturedImageFile = file
                    binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
                    Log.d("AddStoryActivity", file.path)
                }
            }
        }
    companion object {
        const val CAMERA_REQUEST_CODE = 2
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private fun observeSettings() {
        settingsViewModel.loginResultLiveData.observe(this) { newLoginResult ->
            loginResult = newLoginResult ?: LoginResult("GUEST","GUEST","GUEST")
        }
    }

    private fun observeViewModel() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeSettings()
        observeViewModel()
        setupView()
        setupClickListeners()
        if (capturedImageFile == null){
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
        binding.sendStoryButton.text = if (loginResult.token != "GUEST") "Add to Story" else "Add as Guest"
    }
    private fun setupClickListeners() {
        binding.openCamera.setOnClickListener {
            openCamera()
        }
        binding.addDescButton.setOnClickListener {
            showDescriptionInputDialog()
        }
        binding.sendStoryButton.setOnClickListener {
            if (capturedImageFile == null) {
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

        Log.d("AddStoryActivity", storyDescription)
    }

    private fun uploadStory(imageFile: File, description: String) {
        viewModel.addNewStory(imageFile, description, loginResult) { response ->
            if (!response.error) {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, CameraActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera(){
        val intent = Intent(this, CameraActivity::class.java)
        startCameraActivity.launch(intent)
    }
}

