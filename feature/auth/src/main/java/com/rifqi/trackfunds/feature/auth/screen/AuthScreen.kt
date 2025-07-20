package com.rifqi.trackfunds.feature.auth.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.feature.auth.event.AuthEvent
import com.rifqi.trackfunds.feature.auth.state.AuthMode
import com.rifqi.trackfunds.feature.auth.state.AuthUiState
import com.rifqi.trackfunds.feature.auth.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    onNavigateToHome: (AppScreen) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            onNavigateToHome(screen)
        }
    }

    AuthContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AuthContent(
    uiState: AuthUiState,
    onEvent: (AuthEvent) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Judul berubah sesuai mode
            Text(
                text = if (uiState.authMode == AuthMode.LOGIN) "Selamat Datang Kembali" else "Buat Akun Baru",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { onEvent(AuthEvent.TogglePasswordVisibility) }) {
                        Icon(
                            imageVector = if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            )

            // Field Konfirmasi Password hanya muncul di mode Register
            AnimatedVisibility(visible = uiState.authMode == AuthMode.REGISTER) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.confirmPassword,
                        onValueChange = { onEvent(AuthEvent.ConfirmPasswordChanged(it)) },
                        label = { Text("Konfirmasi Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Menampilkan pesan error jika ada
            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
            }

            // Menampilkan loading atau tombol
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { onEvent(AuthEvent.Submit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    // Teks tombol berubah sesuai mode
                    Text(if (uiState.authMode == AuthMode.LOGIN) "Login" else "Register")
                }
            }

            Spacer(Modifier.height(16.dp))

            // Teks untuk beralih mode
            val switchText =
                if (uiState.authMode == AuthMode.LOGIN) "Belum punya akun? Register" else "Sudah punya akun? Login"
            ClickableText(
                text = AnnotatedString(switchText),
                onClick = { onEvent(AuthEvent.SwitchMode) },
                style = TextStyle(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, name = "Login Mode")
@Composable
fun AuthScreenLoginPreview() {
    MaterialTheme {
        AuthContent(uiState = AuthUiState(authMode = AuthMode.LOGIN), onEvent = {})
    }
}

@Preview(showBackground = true, name = "Register Mode")
@Composable
fun AuthScreenRegisterPreview() {
    MaterialTheme {
        AuthContent(uiState = AuthUiState(authMode = AuthMode.REGISTER), onEvent = {})
    }
}