package com.habitflow.app.data.api

import com.habitflow.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API requests/responses
 */
data class PersonaRequest(
    val answers: List<QuestionnaireAnswer>
)

data class PersonaResponse(
    val success: Boolean,
    val persona: Persona
)

data class PersonasResponse(
    val success: Boolean,
    val personas: List<Persona>
)

data class MotivationRequest(
    val personaId: PersonaType,
    val context: String? = null,
    val habitsCompleted: Int? = null,
    val totalHabits: Int? = null
)

data class MotivationResponse(
    val success: Boolean,
    val motivation: AIMotivation
)

data class RecommendationsRequest(
    val personaId: PersonaType,
    val completedHabits: List<String>,
    val context: String? = null
)

data class RecommendationsResponse(
    val success: Boolean,
    val recommendations: List<String>
)

/**
 * HabitFlow API Service
 */
interface HabitFlowApiService {

    @GET("api/health")
    suspend fun healthCheck(): Response<Map<String, Any>>

    @POST("api/persona/determine")
    suspend fun determinePersona(@Body request: PersonaRequest): Response<PersonaResponse>

    @GET("api/persona/all")
    suspend fun getAllPersonas(): Response<PersonasResponse>

    @POST("api/motivation/generate")
    suspend fun generateMotivation(@Body request: MotivationRequest): Response<MotivationResponse>

    @POST("api/habits/recommendations")
    suspend fun getRecommendations(@Body request: RecommendationsRequest): Response<RecommendationsResponse>
}

