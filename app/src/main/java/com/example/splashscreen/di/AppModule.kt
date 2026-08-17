package com.example.splashscreen.di

import android.content.Context
import com.example.splashscreen.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides application-level dependencies.
 *
 * Library:
 * - Dagger Hilt
 *
 * Responsibility:
 * - Provides objects shared by multiple parts of the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Creates the application's SessionManager.
     *
     * SessionManager is a singleton because it represents the
     * application's shared DataStore/session state.
     *
     * @param context application context supplied by Hilt.
     * @return SessionManager instance.
     */
    @Provides
    @Singleton
    fun provideSessionManager(
        @ApplicationContext context: Context
    ): SessionManager {
        return SessionManager(context)
    }
}