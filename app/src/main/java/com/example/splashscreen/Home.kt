
package com.example.splashscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.splashscreen.model.DrawerScreen
import com.example.splashscreen.ui.theme.ZoonyRed
import com.example.splashscreen.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    // Get Context here, inside the Composable.
    // Then use this variable in the button instead of
    // calling LocalContext.current inside onClick.
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedScreen = viewModel.selectedScreen

    val title = when (selectedScreen) {
        DrawerScreen.HOME -> stringResource(R.string.nav_home)
        DrawerScreen.SETTINGS -> stringResource(R.string.nav_settings)
        DrawerScreen.PROFILE -> stringResource(R.string.nav_profile)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                Spacer(Modifier.height(12.dp))

                Text(
                    stringResource(R.string.app_name),
                    fontSize = 20.sp,
                    color = ZoonyRed,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 8.dp
                    )
                )

                HorizontalDivider(
                    Modifier.padding(vertical = 8.dp)
                )

                DrawerItem(
                    screen = DrawerScreen.HOME,
                    selectedScreen = selectedScreen,
                    icon = Icons.Default.Home,
                    labelRes = R.string.nav_home
                ) {
                    viewModel.selectScreen(DrawerScreen.HOME)
                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerItem(
                    screen = DrawerScreen.PROFILE,
                    selectedScreen = selectedScreen,
                    icon = Icons.Default.Person,
                    labelRes = R.string.nav_profile
                ) {
                    viewModel.selectScreen(DrawerScreen.PROFILE)
                    scope.launch {
                        drawerState.close()
                    }
                }

                DrawerItem(
                    screen = DrawerScreen.SETTINGS,
                    selectedScreen = selectedScreen,
                    icon = Icons.Default.Settings,
                    labelRes = R.string.nav_settings
                ) {
                    viewModel.selectScreen(DrawerScreen.SETTINGS)
                    scope.launch {
                        drawerState.close()
                    }
                }

                Spacer(Modifier.weight(1f))

                NavigationDrawerItem(
                    label = {
                        Text(stringResource(R.string.nav_logout))
                    },
                    icon = {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = null
                        )
                    },
                    selected = false,
                    onClick = {
                        viewModel.logout {
                            navController.navigate("login") {
                                popUpTo("home") {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }
        }
    ) {

        Scaffold(
            topBar = {

                TopAppBar(

                    title = {
                        Text(title)
                    },

                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = stringResource(
                                    R.string.cd_menu
                                )
                            )
                        }
                    },

                    actions = {

                        // AR / EN language button
                        TextButton(
                            onClick = {
                                LocaleHelper.toggleLanguage(context)
                            }
                        ) {
                            Text(
                                text =
                                    if (
                                        LocaleHelper.currentLanguage(context) ==
                                        LocaleHelper.LANG_ARABIC
                                    ) {
                                        stringResource(
                                            R.string.language_switch_en
                                        )
                                    } else {
                                        stringResource(
                                            R.string.language_switch_ar
                                        )
                                    },
                                color = Color.White
                            )
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ZoonyRed,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }

        ) { padding ->

            when (selectedScreen) {

                DrawerScreen.HOME -> ProductListScreen(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    showTopBar = false
                )

                DrawerScreen.PROFILE -> Profile(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )

                DrawerScreen.SETTINGS -> Settings(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            }
        }
    }
}


@Composable
private fun DrawerItem(
    screen: DrawerScreen,
    selectedScreen: DrawerScreen,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    labelRes: Int,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(stringResource(labelRes))
        },
        icon = {
            Icon(
                icon,
                contentDescription = null
            )
        },
        selected = selectedScreen == screen,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
            selectedIconColor = ZoonyRed,
            selectedTextColor = ZoonyRed
        ),
        onClick = onClick
    )
}