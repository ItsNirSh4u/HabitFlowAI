/**
 * HabitFlow Unified Backend - Type Definitions
 * Combines types from both backend implementations
 */

// ===== Persona Types =====

export enum PersonaType {
  SUPPORTER = 'SUPPORTER',
  CHALLENGER = 'CHALLENGER'
}

export interface Persona {
  id: PersonaType;
  name: string;
  title: string;
  motivationMsg: string;
  completionMsg: string;
  color: string;
  emoji: string;
}

// ===== Questionnaire Types =====

export interface QuestionnaireAnswer {
  questionId: string;
  answer: string;
}

export interface PersonaRequest {
  answers: QuestionnaireAnswer[];
}

export interface PersonaResponse {
  success: boolean;
  persona: Persona;
}

export interface PersonasResponse {
  success: boolean;
  personas: Persona[];
}

// ===== Habit Types =====

export interface HabitRequest {
  habitName: string;
  completed: boolean;
  personaId: PersonaType;
}

export interface RecommendationsRequest {
  personaId: PersonaType;
  completedHabits: string[];
  context?: string;
}

export interface RecommendationsResponse {
  success: boolean;
  recommendations: string[];
}

// ===== Motivation Types =====

export interface MotivationRequest {
  personaId: PersonaType;
  context?: string;
  habitsCompleted?: number;
  totalHabits?: number;
}

export interface AIMotivation {
  message: string;
  personaId: PersonaType;
  timestamp: string;
}

export interface MotivationResponse {
  success: boolean;
  motivation: AIMotivation;
}

// ===== AI Classification Types (from backend) =====

export interface ClassifyRequest {
  quizAnswers: unknown;
  goalText: string;
}

export interface PersonaResult {
  persona: string;
  reasoning: string;
  theme_config: {
    color: string;
  };
}

// ===== Authentication Types =====

export interface RegisterRequest {
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: {
    id: string;
    email: string;
    persona?: string | null;
  };
}
