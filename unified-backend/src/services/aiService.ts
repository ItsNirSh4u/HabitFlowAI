/**
 * HabitFlow Unified Backend - AI Service
 * Supports both OpenAI and Google Gemini APIs for AI-powered features
 */

import axios from 'axios';
import { GoogleGenerativeAI } from '@google/generative-ai';
import { PersonaType, AIMotivation, MotivationRequest, PersonaResult } from '../types';
import { PersonaService } from './personaService';

export class AIService {
  private static readonly OPENAI_API_KEY = process.env.OPENAI_API_KEY;
  private static readonly GEMINI_API_KEY = process.env.GEMINI_API_KEY;
  private static readonly OPENAI_ENDPOINT = 'https://api.openai.com/v1/chat/completions';

  /**
   * Generate AI-powered motivation message
   * Uses OpenAI if available, falls back to mock
   */
  static async generateMotivation(request: MotivationRequest): Promise<AIMotivation> {
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
      const prompt = this.buildMotivationPrompt(request, persona);

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
   * Classify user persona using Gemini AI
   * Used for advanced persona determination
   */
  static async classifyPersona(quizAnswers: unknown, goalText: string): Promise<PersonaResult> {
    if (!this.GEMINI_API_KEY) {
      throw new Error('GEMINI_API_KEY environment variable is not set');
    }

    try {
      const genAI = new GoogleGenerativeAI(this.GEMINI_API_KEY);
      const model = genAI.getGenerativeModel({ model: 'gemini-1.5-flash' });

      const systemPrompt = 'You are a behavioral expert. Based on the user\'s goal and quiz answers, classify them as: Supporter, Challenger, or Pragmatist. Return ONLY valid JSON (no extra text) with this exact structure: {"persona": "string", "reasoning": "string", "theme_config": {"color": "#hexcolor"}}';
      const userPrompt = `${systemPrompt}\n\nGoal: ${goalText}\nQuiz Answers: ${JSON.stringify(quizAnswers)}`;

      const result = await model.generateContent(userPrompt);
      const response = await result.response;
      const responseText = response.text();

      if (!responseText) {
        throw new Error('Empty response from AI model');
      }

      // Parse JSON response with safe fallback
      let classificationResult: PersonaResult;
      try {
        classificationResult = JSON.parse(responseText);
      } catch {
        // Try to extract JSON from response if it has extra text
        const jsonMatch = responseText.match(/\{[\s\S]*\}/);
        if (jsonMatch) {
          classificationResult = JSON.parse(jsonMatch[0]);
        } else {
          throw new Error('Could not parse JSON response from AI');
        }
      }

      return classificationResult;
    } catch (err) {
      const error = err instanceof Error ? err.message : 'Unknown error';
      console.error('Gemini AI Classification Error:', error);
      throw new Error(`AI Classification failed: ${error}`);
    }
  }

  /**
   * Generate habit recommendations based on completion history
   */
  static async generateHabitRecommendations(
    _personaId: PersonaType,
    completedHabits: string[],
    _context?: string
  ): Promise<string[]> {
    // For POC, return contextual recommendations
    const allHabits = [
      'Drink a glass of water',
      'Physical activity',
      'Read for five minutes',
      'Meditate for 5 minutes',
      'Practice gratitude',
      'Take a break and stretch',
      'Journal your thoughts',
      'Call a friend or family member',
      'Healthy meal preparation',
      'Learn something new'
    ];

    // Filter out already completed habits
    const available = allHabits.filter(h => !completedHabits.includes(h));

    // Return top 3 recommendations
    return available.slice(0, 3);
  }

  /**
   * Build motivation prompt based on request context
   */
  private static buildMotivationPrompt(request: MotivationRequest, _persona: any): string {
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
  private static getMockMotivation(request: MotivationRequest, _persona: any): AIMotivation {
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
