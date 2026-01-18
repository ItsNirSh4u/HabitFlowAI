package com.habitflow.app.data

import com.google.gson.annotations.SerializedName

// ===== Auth Models =====

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val persona: String? = null
)

// ===== Persona Models =====

data class Persona(
    val id: String,
    val name: String,
    val title: String,
    val motivationMsg: String,
    val completionMsg: String,
    val color: String,
    val emoji: String
)

data class PersonasResponse(
    val success: Boolean,
    val personas: List<Persona>
)

data class QuestionnaireAnswer(
    val questionId: String,
    val answer: String
)

data class PersonaRequest(
    val answers: List<QuestionnaireAnswer>
)

data class PersonaResponse(
    val success: Boolean,
    val persona: Persona
)

// ===== Motivation Models =====

data class MotivationRequest(
    val personaId: String,
    val context: String? = null,
    val habitsCompleted: Int? = null,
    val totalHabits: Int? = null
)

data class AIMotivation(
    val message: String,
    val personaId: String,
    val timestamp: String
)

data class MotivationResponse(
    val success: Boolean,
    val motivation: AIMotivation
)

// ===== Error Response =====

data class ErrorResponse(
    val error: String? = null,
    val message: String? = null
)

