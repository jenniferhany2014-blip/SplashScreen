package com.example.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    val themeMode = sessionManager.themeMode

    fun setTheme(mode: String) {
        viewModelScope.launch { sessionManager.setThemeMode(mode) }
    }
}