package com.example.splashscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.splashscreen.ui.theme.SplashScreenTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {

        // Must be before super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            AppRoot(
                sessionManager = sessionManager
            )
        }
    }
}

@Composable
fun AppRoot(
    sessionManager: SessionManager
) {
    val isLoggedIn by sessionManager
        .isLoggedIn
        .collectAsState(initial = null)

    val themeMode by sessionManager
        .themeMode
        .collectAsState(
            initial = SessionManager.THEME_SYSTEM
        )

    val darkTheme = when (themeMode) {
        SessionManager.THEME_DARK -> true
        SessionManager.THEME_LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    SplashScreenTheme(
        darkTheme = darkTheme
    ) {
        if (isLoggedIn == null) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            LoginScreen(
                navController = navController
            )
        }

        composable("signup") {
            SignUpScreen(
                navController = navController
            )
        }

        composable("home") {
            Home(
                navController = navController
            )
        }

        composable(
            route = "product_detail/{productId}",
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val productId = backStackEntry
                .arguments
                ?.getInt("productId")

            if (productId != null) {
                ProductDetailScreen(
                    navController = navController,
                    productId = productId
                )
            }
        }
    }
}