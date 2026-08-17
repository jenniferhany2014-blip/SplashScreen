package com.example.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for application settings.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Kotlin Coroutines
 * - DataStore through SessionManager
 * - Dagger Hilt
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    /**
     * Flow containing the current theme mode.
     */
    val themeMode = sessionManager.themeMode

    /**
     * Updates the application's theme.
     *
     * @param mode new theme mode.
     */
    fun setTheme(
        mode: String
    ) {
        viewModelScope.launch {
            sessionManager.setThemeMode(mode)
        }
    }
}