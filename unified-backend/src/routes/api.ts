/**
 * HabitFlow Unified Backend - API Routes
 * Combines all API endpoints from both backend implementations
 */

import express, { Request, Response } from 'express';
import { PersonaService } from '../services/personaService';
import { AIService } from '../services/aiService';
import { 
  PersonaRequest, 
  MotivationRequest,
  RecommendationsRequest,
  ClassifyRequest,
  PersonaResponse,
  PersonasResponse,
  MotivationResponse,
  RecommendationsResponse
} from '../types';
import isAuth, { AuthRequest } from '../middleware/isAuth';
import User from '../models/User';

const router = express.Router();

// ===== Persona Endpoints =====

/**
 * POST /api/persona/determine
 * Determine user persona based on questionnaire answers
 */
router.post('/persona/determine', async (req: Request, res: Response) => {
  try {
    const { answers } = req.body as PersonaRequest;

    if (!answers || !Array.isArray(answers) || answers.length === 0) {
      return res.status(400).json({ error: 'Invalid request: answers required' });
    }

    const persona = PersonaService.determinePersona(answers);

    const response: PersonaResponse = {
      success: true,
      persona
    };

    return res.json(response);
  } catch (error) {
    console.error('Error determining persona:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

/**
 * GET /api/persona/all
 * Get all available personas
 */
router.get('/persona/all', (_req: Request, res: Response) => {
  try {
    const personas = PersonaService.getAllPersonas();
    
    const response: PersonasResponse = {
      success: true,
      personas
    };

    return res.json(response);
  } catch (error) {
    console.error('Error fetching personas:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// ===== AI Motivation Endpoints =====

/**
 * POST /api/motivation/generate
 * Generate AI-powered motivation message
 */
router.post('/motivation/generate', async (req: Request, res: Response) => {
  try {
    const request = req.body as MotivationRequest;

    if (!request.personaId) {
      return res.status(400).json({ error: 'Invalid request: personaId required' });
    }

    const motivation = await AIService.generateMotivation(request);

    const response: MotivationResponse = {
      success: true,
      motivation
    };

    return res.json(response);
  } catch (error) {
    console.error('Error generating motivation:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// ===== Habit Endpoints =====

/**
 * POST /api/habits/recommendations
 * Get AI-powered habit recommendations
 */
router.post('/habits/recommendations', async (req: Request, res: Response) => {
  try {
    const { personaId, completedHabits, context } = req.body as RecommendationsRequest;

    if (!personaId) {
      return res.status(400).json({ error: 'Invalid request: personaId required' });
    }

    const recommendations = await AIService.generateHabitRecommendations(
      personaId,
      completedHabits || [],
      context
    );

    const response: RecommendationsResponse = {
      success: true,
      recommendations
    };

    return res.json(response);
  } catch (error) {
    console.error('Error generating recommendations:', error);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// ===== AI Classification Endpoint (Protected) =====

/**
 * POST /api/classify
 * AI-powered persona classification using Gemini
 * Requires authentication
 */
router.post('/classify', isAuth, async (req: AuthRequest, res: Response) => {
  try {
    const { quizAnswers, goalText } = req.body as ClassifyRequest;
    const userId = req.user?._id;

    if (!userId) {
      return res.status(401).json({ message: 'User not authenticated' });
    }

    if (!quizAnswers || !goalText) {
      return res.status(400).json({ message: 'Quiz answers and goal text are required' });
    }

    // Classify using Gemini AI
    const classificationResult = await AIService.classifyPersona(quizAnswers, goalText);

    // Update user with assigned persona
    await User.findByIdAndUpdate(userId, { persona: classificationResult.persona });

    return res.json(classificationResult);
  } catch (err) {
    const error = err instanceof Error ? err.message : 'Unknown error';
    console.error('AI Classification Error:', error);
    return res.status(500).json({ 
      error: 'AI Classification failed', 
      details: error
    });
  }
});

// ===== Health Check =====

/**
 * GET /api/health
 * Health check endpoint
 */
router.get('/health', (_req: Request, res: Response) => {
  return res.json({
    success: true,
    status: 'healthy',
    timestamp: new Date().toISOString(),
    features: {
      authentication: true,
      database: true,
      aiMotivation: !!process.env.OPENAI_API_KEY,
      aiClassification: !!process.env.GEMINI_API_KEY
    }
  });
});

export default router;
