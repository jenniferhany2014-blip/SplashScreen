package com.example.splashscreen

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStore used to persist application preferences.
 *
 * Library:
 * - AndroidX DataStore Preferences
 */
private val Context.dataStore by preferencesDataStore(
    name = "zoony_prefs"
)

/**
 * Represents the locally stored user account.
 *
 * @property name user's display name.
 * @property email user's email address.
 * @property password locally stored password.
 */
data class UserAccount(
    val name: String,
    val email: String,
    val password: String
)

/**
 * Manages application session and user preferences.
 *
 * Library:
 * - AndroidX DataStore Preferences
 *
 * Dependency injection:
 * - SessionManager is provided by Hilt's AppModule.
 *
 * Responsibility:
 * - Stores login state.
 * - Stores user account information.
 * - Validates locally stored credentials.
 * - Stores theme preference.
 */
class SessionManager(
    private val context: Context
) {

    companion object {

        private val KEY_IS_LOGGED_IN =
            booleanPreferencesKey("is_logged_in")

        private val KEY_NAME =
            stringPreferencesKey("user_name")

        private val KEY_EMAIL =
            stringPreferencesKey("user_email")

        private val KEY_PASSWORD =
            stringPreferencesKey("user_password")

        private val KEY_THEME =
            stringPreferencesKey("theme_mode")

        const val THEME_SYSTEM = "system"
        const val THEME_LIGHT = "light"
        const val THEME_DARK = "dark"
    }

    /**
     * Emits the current login state.
     */
    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_IS_LOGGED_IN] ?: false
        }

    /**
     * Emits the currently stored user account.
     */
    val user: Flow<UserAccount?> =
        context.dataStore.data.map { prefs ->

            val name = prefs[KEY_NAME]
            val email = prefs[KEY_EMAIL]
            val password = prefs[KEY_PASSWORD]

            if (
                name != null &&
                email != null &&
                password != null
            ) {
                UserAccount(
                    name = name,
                    email = email,
                    password = password
                )
            } else {
                null
            }
        }

    /**
     * Emits the currently selected theme.
     */
    val themeMode: Flow<String> =
        context.dataStore.data.map { prefs ->
            prefs[KEY_THEME] ?: THEME_SYSTEM
        }

    /**
     * Updates the login state.
     *
     * @param value true when the user is logged in.
     */
    suspend fun setLoggedIn(
        value: Boolean
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = value
        }
    }

    /**
     * Saves a user account locally and marks the user as logged in.
     *
     * @param name user's name.
     * @param email user's email.
     * @param password user's password.
     */
    suspend fun saveAccount(
        name: String,
        email: String,
        password: String
    ) {
        context.dataStore.edit { prefs ->

            prefs[KEY_NAME] = name.trim()

            prefs[KEY_EMAIL] =
                email.trim().lowercase()

            prefs[KEY_PASSWORD] = password

            prefs[KEY_IS_LOGGED_IN] = true
        }
    }

    /**
     * Checks whether the supplied credentials match
     * the locally stored account.
     *
     * @param email email entered by the user.
     * @param password password entered by the user.
     * @return true when the credentials match.
     */
    suspend fun validateCredentials(
        email: String,
        password: String
    ): Boolean {

        val account =
            user.first()
                ?: return false

        return account.email.equals(
            email.trim(),
            ignoreCase = true
        ) &&
                account.password == password
    }

    /**
     * Checks whether a local account exists.
     *
     * @return true if an account exists.
     */
    suspend fun accountExists(): Boolean {
        return user.first() != null
    }

    /**
     * Saves the selected theme mode.
     *
     * @param mode theme identifier.
     */
    suspend fun setThemeMode(
        mode: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME] = mode
        }
    }
}