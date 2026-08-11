package com.example.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionManager = SessionManager(application)
    val user = sessionManager.user
}