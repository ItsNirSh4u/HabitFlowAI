package com.habitflow.app.ui.screens
}
    }
        }
            )
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                style = MaterialTheme.typography.bodyLarge,
                text = label,
            Text(

            )
                modifier = Modifier.padding(end = 16.dp)
                fontSize = 32.sp,
                text = emoji,
            Text(
        ) {
            verticalAlignment = Alignment.CenterVertically
                .padding(20.dp),
                .fillMaxWidth()
            modifier = Modifier
        Row(
    ) {
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
        shape = RoundedCornerShape(16.dp),
            .clickable { onClick() },
            )
                shape = RoundedCornerShape(16.dp)
                color = borderColor,
                width = 2.dp,
            .border(
            .fillMaxWidth()
        modifier = Modifier
    Card(

    }
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.primaryContainer
    val backgroundColor = if (isSelected) {

    }
        MaterialTheme.colorScheme.outline
    } else {
        MaterialTheme.colorScheme.primary
    val borderColor = if (isSelected) {
) {
    onClick: () -> Unit
    isSelected: Boolean,
    value: String,
    label: String,
    emoji: String,
fun OptionCard(
@Composable

}
    }
        }
            }
                )
                    style = MaterialTheme.typography.titleMedium
                    text = "Continue",
                Text(
            ) {
                shape = RoundedCornerShape(12.dp)
                    .height(56.dp),
                    .fillMaxWidth()
                modifier = Modifier
                enabled = selectedAnswer != null,
                },
                    }
                        onComplete(listOf(QuestionnaireAnswer("q1", answer)))
                    selectedAnswer?.let { answer ->
                onClick = {
            Button(
            // Continue Button

            }
                )
                    onClick = { selectedAnswer = "B" }
                    isSelected = selectedAnswer == "B",
                    value = "B",
                    label = "B) Competitive challenge (\"Show them who's boss!\")",
                    emoji = "🔥",
                OptionCard(
                // Option B - Challenger

                Spacer(modifier = Modifier.height(16.dp))

                )
                    onClick = { selectedAnswer = "A" }
                    isSelected = selectedAnswer == "A",
                    value = "A",
                    label = "A) Encouraging words (\"You got this!\")",
                    emoji = "💪",
                OptionCard(
                // Option A - Supporter

                )
                    textAlign = TextAlign.Center
                    modifier = Modifier.padding(bottom = 32.dp),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge,
                    text = "When you need to do a difficult task, what motivates you the most?",
                Text(
            ) {
                verticalArrangement = Arrangement.Center
                modifier = Modifier.weight(1f),
            Column(
            // Question Section

            }
                )
                    textAlign = TextAlign.Center
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                    text = "Let's find the perfect way to motivate you",
                Text(

                Spacer(modifier = Modifier.height(16.dp))

                )
                    textAlign = TextAlign.Center
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    text = "Welcome to HabitFlow AI",
                Text(
            ) {
                modifier = Modifier.padding(top = 48.dp)
                horizontalAlignment = Alignment.CenterHorizontally,
            Column(
            // Header
        ) {
            verticalArrangement = Arrangement.SpaceBetween
            horizontalAlignment = Alignment.CenterHorizontally,
                .padding(24.dp),
                .fillMaxSize()
            modifier = Modifier
        Column(
    ) {
        color = MaterialTheme.colorScheme.background
        modifier = Modifier.fillMaxSize(),
    Surface(

    var selectedAnswer by remember { mutableStateOf<String?>(null) }
) {
    onComplete: (List<QuestionnaireAnswer>) -> Unit
fun OnboardingScreen(
@Composable
 */
 * Onboarding Screen - Questionnaire
/**

import com.habitflow.app.data.model.QuestionnaireAnswer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.background


