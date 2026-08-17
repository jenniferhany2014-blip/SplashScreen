package com.example.splashscreen

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {

    const val LANG_ENGLISH = "en"
    const val LANG_ARABIC = "ar"

    private const val LANGUAGE_CHANGED =
        "language_changed"

    fun currentLanguage(context: Context): String {
        return context.resources.configuration
            .locales[0]
            .language
            .let {
                if (it == LANG_ARABIC) {
                    LANG_ARABIC
                } else {
                    LANG_ENGLISH
                }
            }
    }

    fun setLanguage(
        context: Context,
        language: String
    ) {
        val locale = Locale(language)

        Locale.setDefault(locale)

        val configuration =
            Configuration(context.resources.configuration)

        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        context.resources.updateConfiguration(
            configuration,
            context.resources.displayMetrics
        )

        if (context is Activity) {

            // Tell MainActivity that this recreation
            // came from the language button.
            context.intent.putExtra(
                LANGUAGE_CHANGED,
                true
            )

            context.recreate()
        }
    }

    fun toggleLanguage(context: Context) {

        val current =
            currentLanguage(context)

        if (current == LANG_ARABIC) {
            setLanguage(
                context,
                LANG_ENGLISH
            )
        } else {
            setLanguage(
                context,
                LANG_ARABIC
            )
        }
    }

    fun shouldKeepCurrentLanguage(
        intent: android.content.Intent
    ): Boolean {
        return intent.getBooleanExtra(
            LANGUAGE_CHANGED,
            false
        )
    }
}