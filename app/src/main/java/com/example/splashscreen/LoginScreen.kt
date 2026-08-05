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
import com.example.splashscreen.ui.components.LanguageToggleButton
import com.example.splashscreen.ui.components.ZoonyLogo
import com.example.splashscreen.ui.components.ZoonyPrimaryButton
import com.example.splashscreen.ui.components.ZoonyTextField
import com.example.splashscreen.ui.theme.ZoonyBlack
import com.example.splashscreen.ui.theme.ZoonyRed
import com.example.splashscreen.ui.theme.ZoonyTextGray

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val loginSuccessMsg = stringResource(R.string.msg_login_success)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                LanguageToggleButton(textColor = ZoonyRed)
            }

            ZoonyLogo(contentDescription = stringResource(R.string.cd_logo))

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = ZoonyBlack
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.login_subtitle),
                fontSize = 14.sp,
                color = ZoonyTextGray
            )

            Spacer(modifier = Modifier.height(32.dp))

            ZoonyTextField(
                value = viewModel.username,
                onValueChange = viewModel::onUsernameChange,
                label = stringResource(R.string.label_username)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ZoonyTextField(
                value = viewModel.password,
                onValueChange = viewModel::onPasswordChange,
                label = stringResource(R.string.label_password),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            ZoonyPrimaryButton(
                text = stringResource(R.string.btn_login),
                onClick = {
                    if (viewModel.isInputValid()) {
                        Toast.makeText(context, loginSuccessMsg, Toast.LENGTH_SHORT).show()
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { navController.navigate("signup") }) {
                Text(stringResource(R.string.prompt_signup), color = ZoonyBlack)
            }
        }
    }
}