package com.example.splashscreen

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.splashscreen.ui.theme.ZoonyBlack
import com.example.splashscreen.ui.theme.ZoonyRed
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedScreen by remember { mutableStateOf("Home") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Zoony Store",
                    fontSize = 20.sp,
                    color = ZoonyRed,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    selected = selectedScreen == "Home",
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
                        selectedIconColor = ZoonyRed,
                        selectedTextColor = ZoonyRed
                    ),
                    onClick = {
                        selectedScreen = "Home"
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Settings") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                    selected = selectedScreen == "Settings",
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
                        selectedIconColor = ZoonyRed,
                        selectedTextColor = ZoonyRed
                    ),
                    onClick = {
                        selectedScreen = "Settings"
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    selected = selectedScreen == "Profile",
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = ZoonyRed.copy(alpha = 0.12f),
                        selectedIconColor = ZoonyRed,
                        selectedTextColor = ZoonyRed
                    ),
                    onClick = {
                        selectedScreen = "Profile"
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selectedScreen) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
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
                    Text(text = selectedScreen, fontSize = 30.sp, color = ZoonyBlack)
                }
            }
        }
    }
}