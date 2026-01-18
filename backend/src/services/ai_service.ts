import { Router, Request, Response } from 'express';
import { GoogleGenAI } from '@google/genai';
import User from '../models/User';
import isAuth from '../controllers/isAuth';

interface ClassifyRequest extends Request {
  body: {
    quizAnswers: unknown;
    goalText: string;
  };
  user?: {
    _id: string;
  };
}

interface PersonaResult {
  persona: string;
  reasoning: string;
  theme_config: {
    color: string;
  };
}

const router = Router();

// POST /api/classify (Protected)
router.post('/', isAuth as any, async (req: ClassifyRequest, res: Response) => {
  try {
    const { quizAnswers, goalText } = req.body;
    const userId = req.user?._id;

    if (!userId) {
      return res.status(401).json({ message: 'User not authenticated' });
    }

    if (!process.env.GEMINI_API_KEY) {
      throw new Error('GEMINI_API_KEY environment variable is not set');
    }

    const genAI = new GoogleGenAI({
      apiKey: process.env.GEMINI_API_KEY,
    });

    const systemPrompt = 'You are a behavioral expert. Based on the user\'s goal and quiz answers, classify them as: Supporter, Challenger, or Pragmatist. Return ONLY valid JSON (no extra text) with this exact structure: {"persona": "string", "reasoning": "string", "theme_config": {"color": "#hexcolor"}}';
    const userPrompt = `${systemPrompt}\n\nGoal: ${goalText}\nQuiz Answers: ${JSON.stringify(quizAnswers)}`;

    const response = await genAI.models.generateContent({
      model: 'gemini-3-flash-preview',
      contents: userPrompt,
    });

    const responseText = response.text || '';
    
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

    // Update user with assigned persona
    await User.findByIdAndUpdate(userId, { persona: classificationResult.persona });

    res.json(classificationResult);
  } catch (err) {
    const error = err instanceof Error ? err.message : 'Unknown error';
    console.error('AI Classification Error:', error);
    res.status(500).json({ 
      error: 'AI Classification failed', 
      details: error
    });
  }
});

export default router;
