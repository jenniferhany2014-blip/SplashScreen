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
 * ViewModel responsible for account registration.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Jetpack Compose State
 * - Kotlin Coroutines
 * - Dagger Hilt
 *
 * Responsibility:
 * - Stores registration form state.
 * - Validates name, email, and password.
 * - Saves the account through SessionManager.
 * - Converts technical exceptions into user-friendly messages.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

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
        get() = password.any {
            it.isUpperCase()
        }

    val hasLowerCase: Boolean
        get() = password.any {
            it.isLowerCase()
        }

    val hasDigit: Boolean
        get() = password.any {
            it.isDigit()
        }

    val hasSpecialChar: Boolean
        get() = password.any {
            !it.isLetterOrDigit()
        }

    val isPasswordValid: Boolean
        get() =
            hasMinLength &&
                    hasUpperCase &&
                    hasLowerCase &&
                    hasDigit &&
                    hasSpecialChar

    val isEmailValid: Boolean
        get() =
            email.isNotBlank() &&
                    Patterns.EMAIL_ADDRESS
                        .matcher(email.trim())
                        .matches()

    val isNameValid: Boolean
        get() = name.trim().length >= 2

    val passwordsMatch: Boolean
        get() =
            password.isNotEmpty() &&
                    password == confirmPassword

    /**
     * Updates the name field.
     *
     * @param value new name value.
     */
    fun onNameChange(
        value: String
    ) {
        name = value
        errorMessage = ""
    }

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
     * Updates the confirmation password field.
     *
     * @param value new confirmation password value.
     */
    fun onConfirmPasswordChange(
        value: String
    ) {
        confirmPassword = value
        errorMessage = ""
    }

    /**
     * Validates the registration form and saves the account.
     *
     * Validation errors are handled directly because they are
     * user-input problems.
     *
     * Technical exceptions are converted through ErrorMapper.
     *
     * @param onSuccess called after successful registration.
     */
    fun signUp(
        onSuccess: () -> Unit
    ) {

        when {

            !isNameValid -> {
                errorMessage =
                    "Please enter your name."
                return
            }

            !isEmailValid -> {
                errorMessage =
                    "Please enter a valid email address."
                return
            }

            !isPasswordValid -> {
                errorMessage =
                    "Password does not meet all requirements."
                return
            }

            !passwordsMatch -> {
                errorMessage =
                    "Passwords do not match."
                return
            }
        }

        viewModelScope.launch {

            isLoading = true
            errorMessage = ""

            try {

                sessionManager.saveAccount(
                    name = name.trim(),
                    email = email.trim(),
                    password = password
                )

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