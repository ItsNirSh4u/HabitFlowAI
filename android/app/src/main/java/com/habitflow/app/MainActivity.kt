package com.habitflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.habitflow.app.ui.screens.MainScreen
import com.habitflow.app.ui.screens.OnboardingScreen
import com.habitflow.app.ui.theme.HabitFlowTheme
import com.habitflow.app.ui.viewmodel.HabitFlowViewModel
import com.habitflow.app.ui.viewmodel.HabitFlowViewModelFactory

/**
 * Main Activity - Entry point of the app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = application as HabitFlowApplication

        setContent {
            HabitFlowTheme {
                val viewModel: HabitFlowViewModel = viewModel(
                    factory = HabitFlowViewModelFactory(application.repository)
                )

                HabitFlowApp(viewModel)
            }
        }
    }
}

@Composable
fun HabitFlowApp(viewModel: HabitFlowViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val currentPersona by viewModel.currentPersona.collectAsState()
    val habits by viewModel.habits.collectAsState()
    val motivationMessage by viewModel.motivationMessage.collectAsState()
    val showFeedback by viewModel.showFeedback.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (uiState) {
            is HabitFlowViewModel.UiState.Loading -> {
                LoadingScreen()
            }

            is HabitFlowViewModel.UiState.Onboarding -> {
                OnboardingScreen(
                    onComplete = { answers ->
                        viewModel.submitQuestionnaire(answers)
                    }
                )
            }

            is HabitFlowViewModel.UiState.Main -> {
                currentPersona?.let { persona ->
                    MainScreen(
                        persona = persona,
                        habits = habits,
                        showFeedback = showFeedback,
                        motivationMessage = motivationMessage,
                        onHabitToggle = { habitId, completed ->
                            viewModel.toggleHabitCompletion(habitId, completed)
                        },
                        onRequestAIMotivation = {
                            viewModel.requestAIMotivation()
                        },
                        onClearMotivation = {
                            viewModel.clearMotivation()
                        },
                        onResetPersona = {
                            viewModel.resetPersona()
                        }
                    )
                }
            }

            is HabitFlowViewModel.UiState.Error -> {
                ErrorScreen(
                    message = (uiState as HabitFlowViewModel.UiState.Error).message
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading HabitFlow...",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "⚠️",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Oops! Something went wrong",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

