package com.example.splashscreen.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.splashscreen.LocaleHelper

/**
 * EN <-> AR language toggle. Used in Home's top bar and LoginScreen's header.
 * [textColor] lets each screen match its own surface (e.g. white on a colored app bar,
 * brand red on a white background).
 */
@Composable
fun LanguageToggleButton(textColor: Color = Color.White) {
    TextButton(onClick = { LocaleHelper.toggleLanguage() }) {
        Text(
            text = if (LocaleHelper.currentLanguage() == LocaleHelper.LANG_ARABIC) "EN" else "AR",
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}