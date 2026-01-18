package com.habitflow.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Custom Habit Creation Dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitDialog(
    onDismiss: () -> Unit,
    onHabitCreated: (String) -> Unit,
    existingHabits: List<String>,
    accentColor: Color
) {
    var habitName by remember { mutableStateOf("") }
    var showSuggestions by remember { mutableStateOf(true) }

    val habitSuggestions = listOf(
        "🏃 Morning run",
        "💧 Drink 8 glasses of water",
        "📚 Read for 30 minutes",
        "🧘 Meditate for 10 minutes",
        "🥗 Eat a healthy meal",
        "💪 Workout session",
        "📝 Journal thoughts",
        "🌙 Sleep before midnight",
        "📱 Limit screen time",
        "🎨 Creative activity",
        "🌱 Learn something new",
        "🤝 Connect with someone"
    ).filter { suggestion ->
        !existingHabits.any { it.contains(suggestion.substringAfter(" "), ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Create New Habit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Custom input field
                OutlinedTextField(
                    value = habitName,
                    onValueChange = { habitName = it },
                    label = { Text("Habit name") },
                    placeholder = { Text("e.g., Exercise for 30 minutes") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        focusedLabelColor = accentColor,
                        cursorColor = accentColor
                    ),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = null
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Toggle suggestions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showSuggestions = !showSuggestions }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Popular Suggestions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = if (showSuggestions) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (showSuggestions) "Hide" else "Show",
                        tint = accentColor
                    )
                }

                // Suggestions list
                AnimatedVisibility(
                    visible = showSuggestions,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(habitSuggestions) { suggestion ->
                            SuggestionChip(
                                suggestion = suggestion,
                                accentColor = accentColor,
                                onClick = {
                                    habitName = suggestion.substringAfter(" ")
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (habitName.isNotBlank()) {
                        onHabitCreated(habitName.trim())
                        onDismiss()
                    }
                },
                enabled = habitName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    disabledContainerColor = accentColor.copy(alpha = 0.3f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Habit")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SuggestionChip(
    suggestion: String,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = accentColor.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Select",
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * Habit Categories Selector
 */
@Composable
fun HabitCategorySelector(
    categories: List<HabitCategory>,
    selectedCategory: HabitCategory?,
    onCategorySelected: (HabitCategory) -> Unit,
    accentColor: Color
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                CategoryChip(
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onCategorySelected(category) },
                    accentColor = accentColor
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: HabitCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    accentColor: Color
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = category.emoji,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(category.name)
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = accentColor.copy(alpha = 0.2f),
            selectedLabelColor = accentColor
        )
    )
}

/**
 * Data class for habit categories
 */
data class HabitCategory(
    val id: String,
    val name: String,
    val emoji: String
)

val defaultCategories = listOf(
    HabitCategory("health", "Health", "💪"),
    HabitCategory("mind", "Mindfulness", "🧘"),
    HabitCategory("productivity", "Productivity", "📈"),
    HabitCategory("learning", "Learning", "📚"),
    HabitCategory("social", "Social", "🤝"),
    HabitCategory("creative", "Creative", "🎨")
)

