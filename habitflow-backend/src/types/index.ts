/**
 * HabitFlow Backend - TypeScript Type Definitions
 */

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

export interface QuestionnaireAnswer {
  questionId: string;
  answer: string;
}

export interface PersonaRequest {
  answers: QuestionnaireAnswer[];
}

export interface HabitRequest {
  habitName: string;
  completed: boolean;
  personaId: PersonaType;
}

export interface MotivationRequest {
  personaId: PersonaType;
  context?: string;
  habitsCompleted?: number;
  totalHabits?: number;
}

export interface AIMotivationResponse {
  message: string;
  personaId: PersonaType;
  timestamp: string;
}

