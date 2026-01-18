package com.habitflow.app.data.model

/**
 * Persona types available in HabitFlow
 */
enum class PersonaType {
    SUPPORTER,
    CHALLENGER
}

/**
 * User persona data model
 */
data class Persona(
    val id: PersonaType,
    val name: String,
    val title: String,
    val motivationMsg: String,
    val completionMsg: String,
    val color: String,
    val emoji: String
)

/**
 * Questionnaire answer
 */
data class QuestionnaireAnswer(
    val questionId: String,
    val answer: String
)

/**
 * Habit data model
 */
data class Habit(
    val id: Int,
    val name: String,
    val completed: Boolean = false
)

/**
 * AI-generated motivation response
 */
data class AIMotivation(
    val message: String,
    val personaId: PersonaType,
    val timestamp: String
)


