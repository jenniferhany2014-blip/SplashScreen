package com.example.splashscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.splashscreen.ui.theme.ZoonyRed

@Composable
fun Profile(modifier: Modifier = Modifier, viewModel: ProfileViewModel = hiltViewModel()) {
    val user by viewModel.user.collectAsState(initial = null)

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(stringResource(R.string.profile_title), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(24.dp))

        Text(stringResource(R.string.profile_name), style = MaterialTheme.typography.labelLarge)
        Text(user?.name ?: "—", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(18.dp))

        Text(stringResource(R.string.profile_email), style = MaterialTheme.typography.labelLarge)
        Text(user?.email ?: "—", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(18.dp))

        Text(stringResource(R.string.profile_password), style = MaterialTheme.typography.labelLarge)
        Text(if (user?.password.isNullOrEmpty()) "—" else "••••••••", style = MaterialTheme.typography.bodyLarge, color = ZoonyRed)
    }
}