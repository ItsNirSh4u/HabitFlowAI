package com.habitflow.app.data.repository
}
    )
        completed = completed
        name = name,
        id = id,
    private fun HabitEntity.toHabit() = Habit(

    )
        emoji = emoji
        color = color,
        completionMsg = completionMsg,
        motivationMsg = motivationMsg,
        title = title,
        name = name,
        id = PersonaType.valueOf(id),
    private fun PersonaEntity.toPersona() = Persona(

    }
        )
            )
                emoji = persona.emoji
                color = persona.color,
                completionMsg = persona.completionMsg,
                motivationMsg = persona.motivationMsg,
                title = persona.title,
                name = persona.name,
                id = persona.id.name,
            PersonaEntity(
        personaDao.insertPersona(
    private suspend fun savePersonaToDb(persona: Persona) {
    // Helper functions

        }
            }
                e.printStackTrace()
            } catch (e: Exception) {
                }
                    habitDao.updateHabit(it.copy(completed = completed))
                habit?.let {

                val habit = habits.find { it.id == habitId }
                val habits = habitDao.getHabitsForDate(today)
                val today = LocalDate.now().toString()
            try {
        withContext(Dispatchers.IO) {
    suspend fun updateHabitCompletion(habitId: Int, completed: Boolean) =
     */
     * Update habit completion status
    /**

    }
        }
            e.printStackTrace()
        } catch (e: Exception) {
            }
                defaultHabits.forEach { habitDao.insertHabit(it) }
                )
                    HabitEntity(name = "Read for five minutes", date = today)
                    HabitEntity(name = "Physical activity", date = today),
                    HabitEntity(name = "Drink a glass of water", date = today),
                val defaultHabits = listOf(
            if (existing.isEmpty()) {

            val existing = habitDao.getHabitsForDate(today)
            val today = LocalDate.now().toString()
        try {
    suspend fun initializeTodayHabits() = withContext(Dispatchers.IO) {
     */
     * Initialize default habits for today if not exists
    /**

    }
        }
            emptyList()
        } catch (e: Exception) {
            habitDao.getHabitsForDate(today).map { it.toHabit() }
            val today = LocalDate.now().toString()
        try {
    suspend fun getTodayHabits(): List<Habit> = withContext(Dispatchers.IO) {
     */
     * Get habits for today
    /**

    }
        }
            e.printStackTrace()
        } catch (e: Exception) {
            )
                )
                    onboardingCompleted = false
                    selectedPersonaId = null,
                    id = 1,
                UserSettingsEntity(
            settingsDao.saveSettings(
        try {
    suspend fun clearPersona() = withContext(Dispatchers.IO) {
     */
     * Clear persona (logout/reset)
    /**

    }
        }
            e.printStackTrace()
        } catch (e: Exception) {
            )
                )
                    lastUpdated = System.currentTimeMillis()
                    onboardingCompleted = true,
                    selectedPersonaId = personaType.name,
                settings.copy(
            settingsDao.saveSettings(
            )
                onboardingCompleted = false
                selectedPersonaId = null,
                id = 1,
            val settings = settingsDao.getSettings() ?: UserSettingsEntity(
        try {
    suspend fun saveSelectedPersona(personaType: PersonaType) = withContext(Dispatchers.IO) {
     */
     * Save selected persona
    /**

    }
        }
            null
        } catch (e: Exception) {
            }
                personaDao.getPersonaById(personaId)?.toPersona()
            settings?.selectedPersonaId?.let { personaId ->
            val settings = settingsDao.getSettings()
        try {
    suspend fun getSavedPersona(): Persona? = withContext(Dispatchers.IO) {
     */
     * Get saved persona
    /**

    }
        }
            Result.failure(e)
        } catch (e: Exception) {
            }
                Result.failure(Exception("Failed to generate motivation"))
            } else {
                Result.success(response.body()!!.motivation)
            if (response.isSuccessful && response.body() != null) {

            val response = apiService.generateMotivation(request)
            )
                totalHabits = totalHabits
                habitsCompleted = habitsCompleted,
                personaId = personaId,
            val request = MotivationRequest(
        try {
    ): Result<AIMotivation> = withContext(Dispatchers.IO) {
        totalHabits: Int
        habitsCompleted: Int,
        personaId: PersonaType,
    suspend fun generateMotivation(
     */
     * Generate AI motivation message
    /**

    }
        }
            }
                Result.failure(e)
            } catch (dbError: Exception) {
                }
                    Result.failure(e)
                } else {
                    Result.success(localPersonas)
                if (localPersonas.isNotEmpty()) {
                val localPersonas = personaDao.getAllPersonas().map { it.toPersona() }
            try {
            // Fallback to local database on network error
        } catch (e: Exception) {
            }
                }
                    Result.failure(Exception("No personas available"))
                } else {
                    Result.success(localPersonas)
                if (localPersonas.isNotEmpty()) {
                val localPersonas = personaDao.getAllPersonas().map { it.toPersona() }
                // Fallback to local database
            } else {
                Result.success(personas)

                personas.forEach { savePersonaToDb(it) }
                // Cache in database

                val personas = response.body()!!.personas
            if (response.isSuccessful && response.body() != null) {
            val response = apiService.getAllPersonas()
            // Try fetching from API
        try {
    suspend fun getAllPersonas(): Result<List<Persona>> = withContext(Dispatchers.IO) {
     */
     * Get all available personas
    /**

        }
            }
                Result.failure(e)
            } catch (e: Exception) {
                }
                    Result.failure(Exception("Failed to determine persona"))
                } else {
                    Result.success(persona)

                    saveSelectedPersona(persona.id)
                    savePersonaToDb(persona)
                    // Save to local database

                    val persona = response.body()!!.persona
                if (response.isSuccessful && response.body() != null) {
                val response = apiService.determinePersona(PersonaRequest(answers))
            try {
        withContext(Dispatchers.IO) {
    suspend fun determinePersona(answers: List<QuestionnaireAnswer>): Result<Persona> =
     */
     * Determine persona from questionnaire answers
    /**

    }
        }
            false
        } catch (e: Exception) {
            response.isSuccessful
            val response = apiService.healthCheck()
        try {
    suspend fun checkBackendHealth(): Boolean = withContext(Dispatchers.IO) {
     */
     * Check if backend is reachable
    /**

    private val settingsDao = database.userSettingsDao()
    private val habitDao = database.habitDao()
    private val personaDao = database.personaDao()

) {
    private val database: HabitFlowDatabase
    private val apiService: HabitFlowApiService,
class HabitFlowRepository(
 */
 * Repository for HabitFlow data operations
/**

import java.time.LocalDate
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import com.habitflow.app.data.model.*
import com.habitflow.app.data.local.*
import com.habitflow.app.data.api.*


