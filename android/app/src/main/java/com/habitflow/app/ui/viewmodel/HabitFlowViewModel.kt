package com.habitflow.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.habitflow.app.data.model.*
import com.habitflow.app.data.repository.HabitFlowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Main ViewModel for HabitFlow App
 */
class HabitFlowViewModel(
    private val repository: HabitFlowRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _currentPersona = MutableStateFlow<Persona?>(null)
    val currentPersona: StateFlow<Persona?> = _currentPersona.asStateFlow()

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    private val _motivationMessage = MutableStateFlow<String?>(null)
    val motivationMessage: StateFlow<String?> = _motivationMessage.asStateFlow()

    private val _showFeedback = MutableStateFlow(false)
    val showFeedback: StateFlow<Boolean> = _showFeedback.asStateFlow()

    init {
        checkInitialState()
    }

    /**
     * Check if user has completed onboarding
     */
    private fun checkInitialState() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                // Check backend health
                val backendHealthy = repository.checkBackendHealth()

                // Get saved persona
                val savedPersona = repository.getSavedPersona()

                if (savedPersona != null) {
                    _currentPersona.value = savedPersona
                    repository.initializeTodayHabits()
                    loadTodayHabits()
                    _uiState.value = UiState.Main
                } else {
                    _uiState.value = UiState.Onboarding
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Submit questionnaire answers and determine persona
     */
    fun submitQuestionnaire(answers: List<QuestionnaireAnswer>) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            repository.determinePersona(answers)
                .onSuccess { persona ->
                    _currentPersona.value = persona
                    repository.initializeTodayHabits()
                    loadTodayHabits()
                    _uiState.value = UiState.Main
                }
                .onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to determine persona")
                }
        }
    }

    /**
     * Load today's habits
     */
    private fun loadTodayHabits() {
        viewModelScope.launch {
            val todayHabits = repository.getTodayHabits()
            _habits.value = todayHabits
        }
    }

    /**
     * Toggle habit completion
     */
    fun toggleHabitCompletion(habitId: Int, completed: Boolean) {
        viewModelScope.launch {
            repository.updateHabitCompletion(habitId, completed)
            loadTodayHabits()

            // Show feedback message
            if (completed) {
                _showFeedback.value = true

                // Hide after 3 seconds
                kotlinx.coroutines.delay(3000)
                _showFeedback.value = false
            }
        }
    }

    /**
     * Request AI motivation message
     */
    fun requestAIMotivation() {
        viewModelScope.launch {
            val persona = _currentPersona.value ?: return@launch
            val completedCount = _habits.value.count { it.completed }
            val totalCount = _habits.value.size

            repository.generateMotivation(persona.id, completedCount, totalCount)
                .onSuccess { aiMotivation ->
                    _motivationMessage.value = aiMotivation.message
                }
                .onFailure {
                    // Use default motivation on error
                    _motivationMessage.value = persona.motivationMsg
                }
        }
    }

    /**
     * Clear motivation message
     */
    fun clearMotivation() {
        _motivationMessage.value = null
    }

    /**
     * Reset persona (logout)
     */
    fun resetPersona() {
        viewModelScope.launch {
            repository.clearPersona()
            _currentPersona.value = null
            _habits.value = emptyList()
            _uiState.value = UiState.Onboarding
        }
    }

    /**
     * UI State sealed class
     */
    sealed class UiState {
        object Loading : UiState()
        object Onboarding : UiState()
        object Main : UiState()
        data class Error(val message: String) : UiState()
    }
}

/**
 * ViewModel Factory
 */
class HabitFlowViewModelFactory(
    private val repository: HabitFlowRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitFlowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitFlowViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

