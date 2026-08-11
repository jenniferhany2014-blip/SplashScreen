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

    fun onEmailChange(value: String) {
        email = value
        errorMessage = ""
    }

    fun onPasswordChange(value: String) {
        password = value
        errorMessage = ""
    }

    fun login(onSuccess: () -> Unit) {
        // Login only checks that both fields are filled and the email
        // has a normal email format. It does NOT check the saved account
        // and it does NOT apply password requirements.
        if (email.isBlank()) {
            errorMessage = "Please enter your email."
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) {
            errorMessage = "Please enter a valid email address."
            return
        }

        if (password.isBlank()) {
            errorMessage = "Please enter your password."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                // Testing mode: any non-empty password is accepted.
                // The login session is saved so the rest of the app works.
                sessionManager.setLoggedIn(true)
                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Unable to sign in. Please try again."
            } finally {
                isLoading = false
            }
        }
    }
}