package com.example.splashscreen.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.SessionManager
import com.example.splashscreen.model.DrawerScreen
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    var selectedScreen by mutableStateOf(DrawerScreen.HOME)
        private set

    fun selectScreen(screen: DrawerScreen) {
        selectedScreen = screen
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            sessionManager.setLoggedIn(false)
            onLoggedOut()
        }
    }
}