package com.rifqi.trackfunds.feature.auth.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(80.dp))

            Row() {
                Image(
                    painter = painterResource(id = R.drawable.logo_gantenk__1_),
                    contentDescription = "Logo",
                    modifier = Modifier.height(48.dp)
                )
                Text(
                    text = "TrackFunds", // Ganti dengan logo Anda
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(48.dp))

            Text(
                text = if (uiState.authMode == AuthMode.LOGIN) "Login to your account" else "Create your account",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.authMode == AuthMode.LOGIN) "No accounts yet?" else "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(
                    onClick = { onEvent(AuthEvent.SwitchMode) },
                ) {
                    Text(
                        text = if (uiState.authMode == AuthMode.LOGIN) "Sign up here" else "Sign in here",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            AnimatedVisibility(visible = uiState.authMode == AuthMode.REGISTER) {
                Column {
                    GeneralTextInputField(
                        value = uiState.fullName,
                        isError = uiState.fullNameError != null,
                        errorMessage = uiState.fullNameError,
                        onValueChange = { onEvent(AuthEvent.FullNameChanged(it)) },
                        label = "Full Name",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

            GeneralTextInputField(
                value = uiState.email,
                onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
                label = "Email",
                isError = uiState.emailError != null,
                errorMessage = uiState.emailError,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            GeneralTextInputField(
                label = "Password",
                value = uiState.password,
                isError = uiState.passwordError != null,
                errorMessage = uiState.passwordError,
                onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
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

            AnimatedVisibility(visible = uiState.authMode == AuthMode.REGISTER) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    GeneralTextInputField(
                        label = "Confirm Password",
                        value = uiState.confirmPassword,
                        isError = uiState.confirmPasswordError != null,
                        errorMessage = uiState.confirmPasswordError,
                        onValueChange = { onEvent(AuthEvent.ConfirmPasswordChanged(it)) }, // Pastikan ada di AuthEvent
                        visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { onEvent(AuthEvent.TogglePasswordVisibility) }) {
                                Icon(
                                    imageVector = if (uiState.isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = "Toggle password visibility"
                                )
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(visible = uiState.authMode == AuthMode.LOGIN) {
                TextButton(
                    onClick = { onEvent(AuthEvent.ForgotPasswordClicked) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Forgot Password?")
                }
            }

            Spacer(Modifier.height(24.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { onEvent(AuthEvent.Submit) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TrackFundsTheme.extendedColors.accentGreen,
                        contentColor = TrackFundsTheme.extendedColors.onAccentGreen
                    )
                ) {
                    Text(
                        if (uiState.authMode == AuthMode.LOGIN) "Login" else "Sign up",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}


// --- Preview ---
@Preview(showBackground = true, name = "Login Mode")
@Composable
fun AuthScreenLoginPreview() {
    TrackFundsTheme {
        AuthContent(uiState = AuthUiState(authMode = AuthMode.LOGIN), onEvent = {})
    }
}

@Preview(showBackground = true, name = "Register Mode")
@Composable
fun AuthScreenRegisterPreview() {
    TrackFundsTheme {
        AuthContent(uiState = AuthUiState(authMode = AuthMode.REGISTER), onEvent = {})
    }
}