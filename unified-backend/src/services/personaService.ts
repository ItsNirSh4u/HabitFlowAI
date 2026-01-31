/**
 * HabitFlow Unified Backend - Persona Service
 * Manages persona logic and definitions
 */

import { Persona, PersonaType, QuestionnaireAnswer } from '../types';

export const personas: Record<PersonaType, Persona> = {
  [PersonaType.SUPPORTER]: {
    id: PersonaType.SUPPORTER,
    name: "The Supporter",
    title: "You're amazing! I believe in you today!",
    motivationMsg: "Every small step is giant progress. Keep going, you're on the right path!",
    completionMsg: "Excellent! You did it successfully. Great job on your persistence!",
    color: "#FF6B6B",
    emoji: "💪"
  },
  [PersonaType.CHALLENGER]: {
    id: PersonaType.CHALLENGER,
    name: "The Challenger",
    title: "Ready to conquer today? Don't let me down!",
    motivationMsg: "Today you're going to crush a new challenge. Don't fear the difficulty.",
    completionMsg: "Victory! You showed everyone who's boss. Amazing.",
    color: "#4ECDC4",
    emoji: "🔥"
  }
};

export class PersonaService {
  /**
   * Determine persona based on questionnaire answers
   */
  static determinePersona(answers: QuestionnaireAnswer[]): Persona {
    // Simple logic: 'A' = Supporter, 'B' = Challenger
    const mainAnswer = answers.find(a => a.questionId === 'q1');

    if (mainAnswer?.answer === 'A') {
      return personas[PersonaType.SUPPORTER];
    } else if (mainAnswer?.answer === 'B') {
      return personas[PersonaType.CHALLENGER];
    }

    // Default to Supporter
    return personas[PersonaType.SUPPORTER];
  }

  /**
   * Get all available personas
   */
  static getAllPersonas(): Persona[] {
    return Object.values(personas);
  }

  /**
   * Get persona by ID
   */
  static getPersonaById(id: PersonaType): Persona | null {
    return personas[id] || null;
  }

  /**
   * Get persona by name string (for database lookups)
   */
  static getPersonaByName(name: string): Persona | null {
    const personaType = name as PersonaType;
    return personas[personaType] || null;
  }
}
