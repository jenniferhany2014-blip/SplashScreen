package com.example.splashscreen

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class used to initialize Hilt.
 *
 * Library:
 * - Dagger Hilt: com.google.dagger:hilt-android
 *
 * Responsibility:
 * - Creates the application-level Hilt dependency graph.
 * - Allows Hilt to provide dependencies such as repositories,
 *   Retrofit, Room, and SessionManager.
 */
@HiltAndroidApp
class ZoonyApplication : Application()