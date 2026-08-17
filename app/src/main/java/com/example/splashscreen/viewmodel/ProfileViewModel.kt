package com.example.splashscreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for exposing the current user profile.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Kotlin Flow through SessionManager
 * - Dagger Hilt
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    /**
     * Flow containing the currently stored user account.
     */
    val user = sessionManager.user
}