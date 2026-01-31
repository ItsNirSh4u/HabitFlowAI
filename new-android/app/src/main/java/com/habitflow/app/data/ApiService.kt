package com.habitflow.app.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // Auth endpoints
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Persona endpoints
    @GET("persona/all")
    suspend fun getAllPersonas(): Response<PersonasResponse>

    @POST("persona/determine")
    suspend fun determinePersona(@Body request: PersonaRequest): Response<PersonaResponse>

    // Motivation endpoints
    @POST("motivation/generate")
    suspend fun generateMotivation(
        @Header("Authorization") token: String,
        @Body request: MotivationRequest
    ): Response<MotivationResponse>
}

