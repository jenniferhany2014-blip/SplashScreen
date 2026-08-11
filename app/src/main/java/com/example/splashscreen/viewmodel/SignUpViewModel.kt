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

    val hasMinLength: Boolean
        get() = password.length >= 8

    val hasUpperCase: Boolean
        get() = password.any { it.isUpperCase() }

    val hasLowerCase: Boolean
        get() = password.any { it.isLowerCase() }

    val hasDigit: Boolean
        get() = password.any { it.isDigit() }

    val hasSpecialChar: Boolean
        get() = password.any { !it.isLetterOrDigit() }

    val isPasswordValid: Boolean
        get() = hasMinLength &&
                hasUpperCase &&
                hasLowerCase &&
                hasDigit &&
                hasSpecialChar

    val isEmailValid: Boolean
        get() = email.isNotBlank() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()

    val isNameValid: Boolean
        get() = name.trim().length >= 2

    val passwordsMatch: Boolean
        get() = password.isNotEmpty() &&
                password == confirmPassword

    fun onNameChange(value: String) {
        name = value
        errorMessage = ""
    }

    fun onEmailChange(value: String) {
        email = value
        errorMessage = ""
    }

    fun onPasswordChange(value: String) {
        password = value
        errorMessage = ""
    }

    fun onConfirmPasswordChange(value: String) {
        confirmPassword = value
        errorMessage = ""
    }

    fun signUp(onSuccess: () -> Unit) {
        when {
            !isNameValid -> {
                errorMessage = "Please enter your name."
                return
            }

            !isEmailValid -> {
                errorMessage = "Please enter a valid email address."
                return
            }

            !isPasswordValid -> {
                errorMessage = "Password does not meet all requirements."
                return
            }

            !passwordsMatch -> {
                errorMessage = "Passwords do not match."
                return
            }
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = ""

            try {
                // Do not reject an existing account during testing.
                // The newly entered data becomes the saved account.
                sessionManager.saveAccount(
                    name = name,
                    email = email,
                    password = password
                )

                onSuccess()
            } catch (e: Exception) {
                errorMessage = "Unable to create account. Please try again."
            } finally {
                isLoading = false
            }
        }
    }
}