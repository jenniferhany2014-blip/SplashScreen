package com.example.splashscreen

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    const val LANG_ENGLISH = "en"
    const val LANG_ARABIC = "ar"

    private const val PREFS_NAME = "zoony_language"
    private const val KEY_LANGUAGE = "language"

    fun currentLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        return prefs.getString(KEY_LANGUAGE, LANG_ENGLISH)
            ?: LANG_ENGLISH
    }

    fun setLanguage(context: Context, language: String) {
        // English is the default language. Arabic is only selected after
        // the user explicitly presses the AR button.
        val selectedLanguage = if (language == LANG_ARABIC) LANG_ARABIC else LANG_ENGLISH

        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(KEY_LANGUAGE, selectedLanguage)
            .apply()

        applyLanguage(context, selectedLanguage)

        if (context is Activity) {
            context.recreate()
        }
    }

    fun toggleLanguage(context: Context) {
        val current = currentLanguage(context)

        val newLanguage =
            if (current == LANG_ARABIC) {
                LANG_ENGLISH
            } else {
                LANG_ARABIC
            }

        setLanguage(context, newLanguage)
    }

    fun applySavedLanguage(context: Context) {
        val language = currentLanguage(context)
        applyLanguage(context, language)
    }

    private fun applyLanguage(
        context: Context,
        language: String
    ) {
        val locale = Locale(language)

        Locale.setDefault(locale)

        val configuration = Configuration(
            context.resources.configuration
        )

        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        context.resources.updateConfiguration(
            configuration,
            context.resources.displayMetrics
        )
    }
}