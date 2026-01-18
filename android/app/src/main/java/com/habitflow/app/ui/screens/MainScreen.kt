package com.habitflow.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitflow.app.data.model.Habit
import com.habitflow.app.data.model.Persona
import com.habitflow.app.ui.components.AIMotivationDialog
import com.habitflow.app.ui.components.StatisticsCard

/**
 * Main Screen - Habit Tracking
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    persona: Persona,
    habits: List<Habit>,
    showFeedback: Boolean,
    motivationMessage: String?,
    onHabitToggle: (Int, Boolean) -> Unit,
    onRequestAIMotivation: () -> Unit,
    onClearMotivation: () -> Unit,
    onResetPersona: () -> Unit
) {
    val personaColor = Color(android.graphics.Color.parseColor(persona.color))
    val completedCount = habits.count { it.completed }
    val totalCount = habits.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    // AI Dialog State
    var showAIDialog by remember { mutableStateOf(false) }
    var isLoadingAI by remember { mutableStateOf(false) }

    // Show AI Dialog when motivation is requested
    LaunchedEffect(motivationMessage) {
        if (motivationMessage != null) {
            isLoadingAI = false
            showAIDialog = true
        }
    }

    // AI Motivation Dialog
    if (showAIDialog) {
        AIMotivationDialog(
            persona = persona,
            message = motivationMessage,
            isLoading = isLoadingAI,
            onDismiss = {
                showAIDialog = false
                onClearMotivation()
            },
            onRefresh = {
                isLoadingAI = true
                onRequestAIMotivation()
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = persona.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Persona: ${persona.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    Text(
                        text = persona.emoji,
                        fontSize = 40.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = personaColor.copy(alpha = 0.1f)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Statistics Card
            item {
                Spacer(modifier = Modifier.height(8.dp))
                StatisticsCard(
                    completedToday = completedCount,
                    totalToday = totalCount,
                    weekStreak = 3, // TODO: Calculate actual streak
                    personaColor = personaColor
                )
            }

            // Motivation Card with AI Button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = personaColor.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = persona.emoji,
                                fontSize = 40.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Your Motivation",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = personaColor
                                )
                                Text(
                                    text = "\"${persona.motivationMsg}\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                isLoadingAI = true
                                showAIDialog = true
                                onRequestAIMotivation()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = personaColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "AI Motivation",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Get AI Motivation 🤖",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Progress Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Daily Habit Tracking",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$completedCount / $totalCount",
                                style = MaterialTheme.typography.titleMedium,
                                color = personaColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            color = personaColor,
                            trackColor = personaColor.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            // Habits List
            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    personaColor = personaColor,
                    onToggle = { completed ->
                        onHabitToggle(habit.id, completed)
                    }
                )
            }

            // Feedback Message
            if (showFeedback) {
                item {
                    AnimatedVisibility(
                        visible = showFeedback,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = personaColor
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = persona.completionMsg,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                modifier = Modifier.padding(20.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Reset Button
            item {
                OutlinedButton(
                    onClick = onResetPersona,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Choose Another Persona")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun HabitItem(
    habit: Habit,
    personaColor: Color,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (habit.completed) {
                personaColor.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Checkbox(
                    checked = habit.completed,
                    onCheckedChange = { onToggle(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = personaColor
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (habit.completed) FontWeight.SemiBold else FontWeight.Normal
                )
            }

            if (habit.completed) {
                Text(
                    text = "✓ Done",
                    style = MaterialTheme.typography.labelLarge,
                    color = personaColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            personaColor.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

