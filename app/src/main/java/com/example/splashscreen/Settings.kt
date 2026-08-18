package com.example.splashscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themeMode by viewModel.themeMode.collectAsState(
        initial = SessionManager.THEME_SYSTEM
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("favorites")
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.settings_favorites),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.settings_favorites_description),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.settings_theme),
            style = MaterialTheme.typography.titleMedium
        )

        ThemeOption(
            current = themeMode,
            value = SessionManager.THEME_SYSTEM,
            label = stringResource(R.string.theme_system),
            viewModel = viewModel
        )

        ThemeOption(
            current = themeMode,
            value = SessionManager.THEME_LIGHT,
            label = stringResource(R.string.theme_light),
            viewModel = viewModel
        )

        ThemeOption(
            current = themeMode,
            value = SessionManager.THEME_DARK,
            label = stringResource(R.string.theme_dark),
            viewModel = viewModel
        )
    }
}

@Composable
private fun ThemeOption(
    current: String,
    value: String,
    label: String,
    viewModel: SettingsViewModel
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = current == value,
            onClick = { viewModel.setTheme(value) }
        )

        Text(
            text = label,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
