package com.example.splashscreen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.splashscreen.model.DrawerScreen

class HomeViewModel : ViewModel() {

    var selectedScreen by mutableStateOf(DrawerScreen.HOME)
        private set

    fun selectScreen(screen: DrawerScreen) {
        selectedScreen = screen
    }
}