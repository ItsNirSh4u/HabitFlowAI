/**
 * HabitFlow Backend - API Routes
 */

import express, { Request, Response } from 'express';
import { PersonaService } from '../services/personaService';
import { AIService } from '../services/aiService';
import { PersonaRequest, HabitRequest, MotivationRequest } from '../types';

const router = express.Router();

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

    res.json({
      success: true,
      persona
    });
  } catch (error) {
    console.error('Error determining persona:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

/**
 * GET /api/persona/all
 * Get all available personas
 */
router.get('/persona/all', (req: Request, res: Response) => {
  try {
    const personas = PersonaService.getAllPersonas();
    res.json({
      success: true,
      personas
    });
  } catch (error) {
    console.error('Error fetching personas:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

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

    res.json({
      success: true,
      motivation
    });
  } catch (error) {
    console.error('Error generating motivation:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

/**
 * POST /api/habits/recommendations
 * Get AI-powered habit recommendations
 */
router.post('/habits/recommendations', async (req: Request, res: Response) => {
  try {
    const { personaId, completedHabits, context } = req.body;

    if (!personaId) {
      return res.status(400).json({ error: 'Invalid request: personaId required' });
    }

    const recommendations = await AIService.generateHabitRecommendations(
      personaId,
      completedHabits || [],
      context
    );

    res.json({
      success: true,
      recommendations
    });
  } catch (error) {
    console.error('Error generating recommendations:', error);
    res.status(500).json({ error: 'Internal server error' });
  }
});

/**
 * GET /api/health
 * Health check endpoint
 */
router.get('/health', (req: Request, res: Response) => {
  res.json({
    success: true,
    status: 'healthy',
    timestamp: new Date().toISOString()
  });
});

export default router;

