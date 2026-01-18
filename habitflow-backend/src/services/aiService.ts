/**
 * HabitFlow Backend - AI Service
 * Handles AI-powered motivation messages and recommendations
 */

import axios from 'axios';
import { PersonaType, AIMotivationResponse, MotivationRequest } from '../types';
import { PersonaService } from './personaService';

export class AIService {
  private static readonly OPENAI_API_KEY = process.env.OPENAI_API_KEY;
  private static readonly OPENAI_ENDPOINT = 'https://api.openai.com/v1/chat/completions';

  /**
   * Generate AI-powered motivation message
   */
  static async generateMotivation(request: MotivationRequest): Promise<AIMotivationResponse> {
    const persona = PersonaService.getPersonaById(request.personaId);

    if (!persona) {
      throw new Error('Invalid persona ID');
    }

    // If OpenAI API key is not configured, return enhanced mock response
    if (!this.OPENAI_API_KEY) {
      return this.getMockMotivation(request, persona);
    }

    try {
      // Call OpenAI API for personalized motivation
      const prompt = this.buildPrompt(request, persona);

      const response = await axios.post(
        this.OPENAI_ENDPOINT,
        {
          model: 'gpt-3.5-turbo',
          messages: [
            {
              role: 'system',
              content: `You are a motivational coach with the personality of "${persona.name}". Your style is: ${persona.motivationMsg}. Keep responses under 50 words, uplifting and actionable.`
            },
            {
              role: 'user',
              content: prompt
            }
          ],
          max_tokens: 100,
          temperature: 0.8
        },
        {
          headers: {
            'Authorization': `Bearer ${this.OPENAI_API_KEY}`,
            'Content-Type': 'application/json'
          }
        }
      );

      const aiMessage = response.data.choices[0]?.message?.content || persona.motivationMsg;

      return {
        message: aiMessage.trim(),
        personaId: request.personaId,
        timestamp: new Date().toISOString()
      };
    } catch (error) {
      console.error('OpenAI API error:', error);
      // Fallback to enhanced mock response
      return this.getMockMotivation(request, persona);
    }
  }

  /**
   * Generate habit recommendations based on completion history
   */
  static async generateHabitRecommendations(
    personaId: PersonaType,
    completedHabits: string[],
    context?: string
  ): Promise<string[]> {
    // For POC, return contextual recommendations
    const allHabits = [
      'Drink a glass of water',
      'Physical activity',
      'Read for five minutes',
      'Meditate for 5 minutes',
      'Practice gratitude',
      'Take a break and stretch'
    ];

    // Filter out already completed habits
    const available = allHabits.filter(h => !completedHabits.includes(h));

    // Return top 3 recommendations
    return available.slice(0, 3);
  }

  /**
   * Build AI prompt based on request context
   */
  private static buildPrompt(request: MotivationRequest, persona: any): string {
    let prompt = `Generate a motivational message for someone working on their daily habits.`;

    if (request.habitsCompleted !== undefined && request.totalHabits !== undefined) {
      prompt += ` They have completed ${request.habitsCompleted} out of ${request.totalHabits} habits today.`;
    }

    if (request.context) {
      prompt += ` Context: ${request.context}`;
    }

    return prompt;
  }

  /**
   * Get enhanced mock motivation (when API key not available)
   */
  private static getMockMotivation(request: MotivationRequest, persona: any): AIMotivationResponse {
    const messages: Record<PersonaType, string[]> = {
      [PersonaType.SUPPORTER]: [
        "You're doing amazing! Each habit brings you closer to your best self. Keep that positive energy flowing! 💪",
        "I'm so proud of your dedication! Remember, progress over perfection. You've got this!",
        "Your commitment is inspiring! Every small win is a step toward greatness. Believe in yourself!"
      ],
      [PersonaType.CHALLENGER]: [
        "Time to dominate! Show those habits who's boss. No excuses, just results! 🔥",
        "You didn't come this far to only come this far. Push harder, aim higher, crush it!",
        "Winners find ways, losers find excuses. You're a winner. Prove it today!"
      ]
    };

    const personaMessages = messages[request.personaId] || messages[PersonaType.SUPPORTER];
    const randomMessage = personaMessages[Math.floor(Math.random() * personaMessages.length)];

    return {
      message: randomMessage,
      personaId: request.personaId,
      timestamp: new Date().toISOString()
    };
  }
}

