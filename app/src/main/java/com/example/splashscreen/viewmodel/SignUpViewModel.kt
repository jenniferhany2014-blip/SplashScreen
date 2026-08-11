package com.example.splashscreen.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.SessionManager
import kotlinx.coroutines.launch

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set
    var errorMessage by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set

    val hasMinLength get() = password.length >= 8
    val hasUpperCase get() = password.any { it.isUpperCase() }
    val hasLowerCase get() = password.any { it.isLowerCase() }
    val hasDigit get() = password.any { it.isDigit() }
    val hasSpecialChar get() = password.any { !it.isLetterOrDigit() }
    val isPasswordValid get() = hasMinLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
    val isEmailValid get() = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val isNameValid get() = name.trim().length >= 2
    val passwordsMatch get() = password.isNotEmpty() && password == confirmPassword

    fun onNameChange(v: String) { name = v; errorMessage = "" }
    fun onEmailChange(v: String) { email = v; errorMessage = "" }
    fun onPasswordChange(v: String) { password = v; errorMessage = "" }
    fun onConfirmPasswordChange(v: String) { confirmPassword = v; errorMessage = "" }

    fun signUp(onSuccess: () -> Unit) {
        when {
            !isNameValid -> { errorMessage = "Please enter your name."; return }
            !isEmailValid -> { errorMessage = "Please enter a valid email address."; return }
            !isPasswordValid -> { errorMessage = "Password does not meet all requirements."; return }
            !passwordsMatch -> { errorMessage = "Passwords do not match."; return }
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                if (sessionManager.accountExists()) {
                    errorMessage = "An account already exists. Please log in."
                } else {
                    sessionManager.saveAccount(name, email, password)
                    onSuccess()
                }
            } catch (e: Exception) {
                errorMessage = "Unable to create account. Please try again."
            } finally {
                isLoading = false
            }
        }
    }
}