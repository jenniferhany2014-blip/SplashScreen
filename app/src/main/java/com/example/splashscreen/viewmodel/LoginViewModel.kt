package com.example.splashscreen.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.SessionManager
import com.example.splashscreen.error.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for the login screen.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Kotlin Coroutines
 * - Jetpack Compose State
 * - Dagger Hilt
 *
 * Responsibility:
 * - Stores login form state.
 * - Validates user input.
 * - Validates credentials through SessionManager.
 * - Handles application errors.
 * - Exposes loading and error state to the UI.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var errorMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    /**
     * Updates the email field.
     *
     * @param value new email value.
     */
    fun onEmailChange(
        value: String
    ) {
        email = value
        errorMessage = ""
    }

    /**
     * Updates the password field.
     *
     * @param value new password value.
     */
    fun onPasswordChange(
        value: String
    ) {
        password = value
        errorMessage = ""
    }

    /**
     * Validates the login form and checks stored credentials.
     *
     * Validation errors are handled directly because they are
     * user-input problems rather than technical exceptions.
     *
     * Unexpected/data errors are converted using ErrorMapper.
     *
     * @param onSuccess called after successful authentication.
     */
    fun login(
        onSuccess: () -> Unit
    ) {

        if (email.isBlank()) {
            errorMessage = "Please enter your email."
            return
        }

        if (
            !Patterns.EMAIL_ADDRESS
                .matcher(email.trim())
                .matches()
        ) {
            errorMessage =
                "Please enter a valid email address."
            return
        }

        if (password.isBlank()) {
            errorMessage =
                "Please enter your password."
            return
        }

        viewModelScope.launch {

            isLoading = true
            errorMessage = ""

            try {

                val valid =
                    sessionManager.validateCredentials(
                        email = email.trim(),
                        password = password
                    )

                if (!valid) {
                    errorMessage =
                        "Incorrect email or password."

                    return@launch
                }

                sessionManager.setLoggedIn(true)

                onSuccess()

            } catch (e: Exception) {

                val appError =
                    ErrorMapper.map(e)

                errorMessage =
                    ErrorMapper.userMessage(appError)

            } finally {

                isLoading = false
            }
        }
    }
}