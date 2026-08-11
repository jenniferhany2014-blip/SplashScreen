package com.example.splashscreen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.splashscreen.ui.components.ZoonyLogo
import com.example.splashscreen.ui.components.ZoonyPrimaryButton
import com.example.splashscreen.ui.components.ZoonyTextField
import com.example.splashscreen.ui.theme.ZoonyBlack
import com.example.splashscreen.ui.theme.ZoonyRed
import com.example.splashscreen.ui.theme.ZoonyTextGray
import com.example.splashscreen.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    var showValidation by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ZoonyLogo(
                contentDescription = stringResource(R.string.cd_logo)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = ZoonyBlack
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                fontSize = 14.sp,
                color = ZoonyTextGray
            )

            Spacer(Modifier.height(32.dp))

            ZoonyTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                    showValidation = false
                },
                label = stringResource(R.string.label_email),
                isError = showValidation && viewModel.email.isBlank()
            )

            Spacer(Modifier.height(16.dp))

            ZoonyTextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                    showValidation = false
                },
                label = stringResource(R.string.label_password),
                isPassword = true,
                isError = showValidation && viewModel.password.isBlank()
            )

            if (showValidation && viewModel.errorMessage.isNotBlank()) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = viewModel.errorMessage,
                    color = ZoonyRed,
                    fontSize = 13.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            ZoonyPrimaryButton(
                text = if (viewModel.isLoading) {
                    stringResource(R.string.loading)
                } else {
                    stringResource(R.string.btn_login)
                },
                onClick = {
                    showValidation = true

                    viewModel.login {
                        Toast.makeText(
                            context,
                            R.string.msg_login_success,
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.navigate("home") {
                            popUpTo("login") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navController.navigate("signup")
                }
            ) {
                Text(
                    text = stringResource(R.string.prompt_signup),
                    color = ZoonyBlack
                )
            }
        }
    }
}