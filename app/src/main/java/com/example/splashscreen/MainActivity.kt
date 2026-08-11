package com.example.splashscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.splashscreen.ui.theme.SplashScreenTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext
    val sessionManager = SessionManager(context)
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = null)
    val themeMode by sessionManager.themeMode.collectAsState(initial = SessionManager.THEME_SYSTEM)

    val darkTheme = when (themeMode) {
        SessionManager.THEME_DARK -> true
        SessionManager.THEME_LIGHT -> false
        else -> androidx.compose.foundation.isSystemInDarkTheme()
    }

    SplashScreenTheme(darkTheme = darkTheme) {
        if (isLoggedIn == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            AppNavigation(startLoggedIn = isLoggedIn == true)
        }
    }
}

@Composable
fun AppNavigation(startLoggedIn: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (startLoggedIn) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("home") { Home(navController) }
        composable("detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                ProductDetailScreen(navController, productId)
            }
        }
    }
}