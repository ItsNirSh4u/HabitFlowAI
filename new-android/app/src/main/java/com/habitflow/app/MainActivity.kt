package com.habitflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.habitflow.app.data.TokenManager
import com.habitflow.app.ui.screens.HomeScreen
import com.habitflow.app.ui.screens.LoginScreen
import com.habitflow.app.ui.theme.HabitFlowAITheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(applicationContext)

        // Check if user is already logged in
        val initialRoute = runBlocking {
            val token = tokenManager.tokenFlow.firstOrNull()
            if (token.isNullOrEmpty()) "login" else "home"
        }

        setContent {
            HabitFlowAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HabitFlowNavigation(
                        tokenManager = tokenManager,
                        startDestination = initialRoute
                    )
                }
            }
        }
    }
}

@Composable
fun HabitFlowNavigation(
    tokenManager: TokenManager,
    startDestination: String
) {
    val navController = rememberNavController()

    // Collect user email for display
    val userEmail by tokenManager.userEmailFlow.collectAsState(initial = "")

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                tokenManager = tokenManager,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                tokenManager = tokenManager,
                userEmail = userEmail ?: "User",
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}

