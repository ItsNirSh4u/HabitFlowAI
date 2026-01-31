package com.habitflow.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.habitflow.app.data.LoginRequest
import com.habitflow.app.data.RegisterRequest
import com.habitflow.app.data.RetrofitClient
import com.habitflow.app.data.TokenManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    tokenManager: TokenManager,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isRegistering by remember { mutableStateOf(false) }
    var shouldNavigate by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Handle navigation via LaunchedEffect to ensure it's on the main thread
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "HabitFlow AI",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isRegistering) "Create Account" else "Welcome Back",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = null

                    try {
                        if (isRegistering) {
                            // Skip API call entirely - just use mock data
                            val fakeUserId = java.util.UUID.randomUUID().toString()
                            tokenManager.saveAuthData(
                                token = "mock_token_${System.currentTimeMillis()}",
                                userId = fakeUserId,
                                email = email
                            )
                            // Set loading false before navigation
                            isLoading = false
                            shouldNavigate = true
                            return@launch
                        } else {
                            val response = RetrofitClient.apiService.login(LoginRequest(email, password))
                            if (response.isSuccessful && response.body() != null) {
                                val authResponse = response.body()!!
                                tokenManager.saveAuthData(
                                    token = authResponse.token,
                                    userId = authResponse.user.id,
                                    email = authResponse.user.email
                                )
                                isLoading = false
                                shouldNavigate = true
                                return@launch
                            } else {
                                errorMessage = response.errorBody()?.string() ?: "Authentication failed"
                            }
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "An error occurred"
                    }
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading && email.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (isRegistering) "Register" else "Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                isRegistering = !isRegistering
                errorMessage = null
            },
            enabled = !isLoading
        ) {
            Text(
                if (isRegistering) "Already have an account? Login"
                else "Don't have an account? Register"
            )
        }
    }
}

