package com.example.splashscreen
import com.example.splashscreen.model.DrawerScreen
import com.example.splashscreen.viewmodel.HomeViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.splashscreen.ui.components.LanguageToggleButton
import com.example.splashscreen.ui.theme.ZoonyBlack
import com.example.splashscreen.ui.theme.ZoonyRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeViewModel = viewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedScreen = viewModel.selectedScreen

    val selectedScreenTitle = when (selectedScreen) {
        DrawerScreen.HOME -> stringResource(R.string.nav_home)
        DrawerScreen.SETTINGS -> stringResource(R.string.nav_settings)
        DrawerScreen.PROFILE -> stringResource(R.string.nav_profile)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 20.sp,
                    color = ZoonyRed,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_home)) },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = selectedScreen == DrawerScreen.HOME,
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
                        selectedIconColor = ZoonyRed,
                        selectedTextColor = ZoonyRed
                    ),
                    onClick = {
                        viewModel.selectScreen(DrawerScreen.HOME)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_settings)) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    selected = selectedScreen == DrawerScreen.SETTINGS,
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
                        selectedIconColor = ZoonyRed,
                        selectedTextColor = ZoonyRed
                    ),
                    onClick = {
                        viewModel.selectScreen(DrawerScreen.SETTINGS)
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.nav_profile)) },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = selectedScreen == DrawerScreen.PROFILE,
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
                        selectedIconColor = ZoonyRed,
                        selectedTextColor = ZoonyRed
                    ),
                    onClick = {
                        viewModel.selectScreen(DrawerScreen.PROFILE)
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedScreenTitle) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.cd_menu))
                        }
                    },
                    actions = { LanguageToggleButton(textColor = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ZoonyRed,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            containerColor = Color.White
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = selectedScreenTitle, fontSize = 30.sp, color = ZoonyBlack)
                }
            }
        }
    }
}