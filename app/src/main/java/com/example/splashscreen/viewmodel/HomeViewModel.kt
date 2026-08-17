package com.example.splashscreen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splashscreen.SessionManager
import com.example.splashscreen.model.DrawerScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for Home screen state and navigation drawer state.
 *
 * Libraries:
 * - AndroidX Lifecycle ViewModel
 * - Jetpack Compose
 * - Kotlin Coroutines
 * - Dagger Hilt
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    var selectedScreen by mutableStateOf(
        DrawerScreen.HOME
    )
        private set

    /**
     * Changes the currently selected drawer screen.
     *
     * @param screen screen selected by the user.
     */
    fun selectScreen(
        screen: DrawerScreen
    ) {
        selectedScreen = screen
    }

    /**
     * Logs the user out and invokes the supplied callback.
     *
     * @param onLoggedOut called after logout completes.
     */
    fun logout(
        onLoggedOut: () -> Unit
    ) {
        viewModelScope.launch {

            sessionManager.setLoggedIn(false)

            onLoggedOut()
        }
    }
}