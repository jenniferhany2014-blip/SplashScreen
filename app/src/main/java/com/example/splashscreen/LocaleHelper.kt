package com.example.splashscreen

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/**
 * Central place for reading/writing the app's per-app language.
 * Uses the AndroidX per-app language API (appcompat 1.6.0+), which
 * works with plain ComponentActivity — no need to extend AppCompatActivity.
 *
 * Requires in AndroidManifest.xml (inside <application>):
 *   <meta-data
 *       android:name="autoStoreLocales"
 *       android:value="true" />
 *
 * Requires in build.gradle(.kts):
 *   implementation("androidx.appcompat:appcompat:1.7.0")
 */
object LocaleHelper {

    const val LANG_ENGLISH = "en"
    const val LANG_ARABIC = "ar"

    /** Returns "en" or "ar" — defaults to "en" if no override is set (system default). */
    fun currentLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        return if (!locales.isEmpty) locales[0]?.language ?: LANG_ENGLISH else LANG_ENGLISH
    }

    /** Applies the given language code app-wide. The current Activity recreates automatically. */
    fun setLanguage(languageCode: String) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(languageCode)
        )
    }

    /** Toggles between English and Arabic. */
    fun toggleLanguage() {
        val next = if (currentLanguage() == LANG_ARABIC) LANG_ENGLISH else LANG_ARABIC
        setLanguage(next)
    }
}