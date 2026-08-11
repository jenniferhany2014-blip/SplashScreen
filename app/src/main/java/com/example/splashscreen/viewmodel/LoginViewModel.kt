package com.example.splashscreen.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var errorMessage by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set

    val hasMinLength: Boolean get() = password.length >= 8
    val hasUpperCase: Boolean get() = password.any { it.isUpperCase() }
    val hasLowerCase: Boolean get() = password.any { it.isLowerCase() }
    val hasDigit: Boolean get() = password.any { it.isDigit() }
    val hasSpecialChar: Boolean get() = password.any { !it.isLetterOrDigit() }
    val isPasswordValid: Boolean
        get() = hasMinLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar

    fun onEmailChange(value: String) { email = value; errorMessage = "" }
    fun onPasswordChange(value: String) { password = value; errorMessage = "" }

    fun login(onSuccess: () -> Unit) {
        if (email.isBlank()) { errorMessage = "Please enter your email."; return }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            errorMessage = "Please enter a valid email address."; return
        }
        if (!isPasswordValid) { errorMessage = "Password does not meet the requirements."; return }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""
            try {
                if (sessionManager.validateCredentials(email, password)) {
                    sessionManager.setLoggedIn(true)
                    onSuccess()
                } else {
                    errorMessage = "Incorrect email or password."
                }
            } catch (e: Exception) {
                errorMessage = "Unable to sign in. Please try again."
            } finally {
                isLoading = false
            }
        }
    }
}