package com.example.splashscreen.ui.theme

import androidx.compose.ui.graphics.Color

// Zoony Store brand palette — pulled from the logo (red / black / white)
val ZoonyRed = Color(0xFFE4001C)        // primary — matches the logo's red
val ZoonyRedDark = Color(0xFFB3001A)    // pressed / darker variant
val ZoonyBlack = Color(0xFF0D0D0D)      // secondary — matches the logo's black shadow
val ZoonyWhite = Color(0xFFFFFFFF)      // background — matches the logo's white space
val ZoonyGray = Color(0xFFF5F5F5)       // subtle surface / card background
val ZoonyTextGray = Color(0xFF6E6E6E)   // secondary text / hints

// Kept for backwards compatibility with existing references (e.g. Home.kt)
val GreenJC = ZoonyRed