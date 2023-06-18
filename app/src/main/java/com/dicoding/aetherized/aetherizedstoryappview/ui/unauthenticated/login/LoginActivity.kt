package com.dicoding.aetherized.aetherizedstoryappview.ui.unauthenticated.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.dicoding.aetherized.aetherizedstoryappview.model.user.LoginResult
import com.dicoding.aetherized.aetherizedstoryappview.databinding.ActivityLoginBinding
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.home.HomeActivity
import com.dicoding.aetherized.aetherizedstoryappview.ui.authenticated.settings.SettingsViewModel
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.CustomPreference
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.MyApplication
import com.dicoding.aetherized.aetherizedstoryappview.util.helper.ViewModelFactory


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val preferenceDataStore by lazy { (application as MyApplication).customPreference }

    private val viewModelFactory by lazy { ViewModelFactory(preferenceDataStore) }
    private val viewModel by viewModels<LoginViewModel> { viewModelFactory }
    private val settingsViewModel by viewModels<SettingsViewModel> { viewModelFactory }

    private lateinit var loginResult: LoginResult

    private var emailForm = false
    private var passForm = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        observeSettings()
        observeViewModel()
        playAnimation()
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

    private fun observeSettings() {
        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun observeViewModel() {
        loginResult = LoginResult()
        viewModel.loginResult.observe(this) {
            loginResult = it!!
        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAction() {
        binding.emailEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                emailForm = isValidEmail(s.toString())
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {

            }
        })
        binding.passwordEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                passForm = isValidPassword(s.toString())
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {

            }
        })
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().lowercase()
            val password = binding.passwordEditText.text.toString()

            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = "Fill the email address"
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = "Fill the password"
                }
                else -> {
                    viewModel.login(email, password) { response ->
                        if (!response.error) {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        binding.loginGuestButton.setOnClickListener {
            viewModel.loginGuest {
                Toast.makeText(this, "LOGGED IN AS GUEST", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }

    private fun setMyButtonEnable() {
        val formFilled = emailForm && passForm
        binding.loginButton.isEnabled = formFilled
    }
    private fun isValidPassword(password: String): Boolean {
        return (password.length > 7)
    }
    private fun isValidEmail(email: String): Boolean {
        return email.length >= 5
    }
    private fun playAnimation() {
        val translationAnimator = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -60f, 60f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val rotateClockwise = ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0f, 40f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val rotateCounterClockwise = ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0f, -40f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        AnimatorSet().apply {
            playTogether(translationAnimator, rotateCounterClockwise, rotateClockwise)
            start()
        }


        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val loginGuest = ObjectAnimator.ofFloat(binding.loginGuestButton, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val pass = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)


        AnimatorSet().apply {
            playSequentially(email, pass, login, loginGuest)
            start()
        }
    }
}