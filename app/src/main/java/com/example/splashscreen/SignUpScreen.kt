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
import com.example.splashscreen.ui.components.ZoonyTextField
import com.example.splashscreen.ui.theme.ZoonyBlack
import com.example.splashscreen.ui.theme.ZoonyRed
import com.example.splashscreen.ui.theme.ZoonyTextGray
import com.example.splashscreen.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
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
            Text(
                text = stringResource(R.string.signup_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = ZoonyBlack
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.signup_subtitle),
                fontSize = 14.sp,
                color = ZoonyTextGray
            )

            Spacer(Modifier.height(24.dp))

            ZoonyTextField(
                value = viewModel.name,
                onValueChange = {
                    viewModel.onNameChange(it)
                    showValidation = false
                },
                label = stringResource(R.string.label_name)
            )

            Spacer(Modifier.height(12.dp))

            ZoonyTextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.onEmailChange(it)
                    showValidation = false
                },
                label = stringResource(R.string.label_email)
            )

            Spacer(Modifier.height(12.dp))

            ZoonyTextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.onPasswordChange(it)
                    showValidation = false
                },
                label = stringResource(R.string.label_password),
                isPassword = true
            )

            Spacer(Modifier.height(12.dp))

            ZoonyTextField(
                value = viewModel.confirmPassword,
                onValueChange = {
                    viewModel.onConfirmPasswordChange(it)
                    showValidation = false
                },
                label = stringResource(R.string.label_confirm_password),
                isPassword = true
            )

            if (showValidation && viewModel.errorMessage.isNotBlank()) {
                Spacer(Modifier.height(8.dp))

                Text(
                    text = viewModel.errorMessage,
                    color = ZoonyRed,
                    fontSize = 13.sp
                )
            }

            if (showValidation) {
                Spacer(Modifier.height(8.dp))
                PasswordRequirements(viewModel)
            }

            Spacer(Modifier.height(20.dp))

            Button(
                enabled = !viewModel.isLoading,
                onClick = {
                    showValidation = true

                    viewModel.signUp {
                        Toast.makeText(
                            context,
                            R.string.msg_signup_success,
                            Toast.LENGTH_SHORT
                        ).show()

                        navController.navigate("home") {
                            popUpTo("signup") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ZoonyRed
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = if (viewModel.isLoading) {
                        stringResource(R.string.loading)
                    } else {
                        stringResource(R.string.btn_signup)
                    },
                    color = Color.White
                )
            }

            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    text = stringResource(R.string.prompt_login),
                    color = ZoonyBlack
                )
            }
        }
    }
}

@Composable
private fun PasswordRequirements(
    viewModel: SignUpViewModel
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.password_requirements_title),
            color = ZoonyBlack,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        RequirementRow(
            viewModel.hasMinLength,
            stringResource(R.string.password_min_length)
        )

        RequirementRow(
            viewModel.hasUpperCase,
            stringResource(R.string.password_uppercase)
        )

        RequirementRow(
            viewModel.hasLowerCase,
            stringResource(R.string.password_lowercase)
        )

        RequirementRow(
            viewModel.hasDigit,
            stringResource(R.string.password_digit)
        )

        RequirementRow(
            viewModel.hasSpecialChar,
            stringResource(R.string.password_special)
        )

        RequirementRow(
            viewModel.passwordsMatch,
            stringResource(R.string.password_match)
        )
    }
}

@Composable
private fun RequirementRow(
    valid: Boolean,
    text: String
) {
    Text(
        text = if (valid) "✓ $text" else "• $text",
        color = if (valid) ZoonyRed else ZoonyTextGray,
        fontSize = 12.sp
    )
}