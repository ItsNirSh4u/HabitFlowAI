import { Router, Request, Response, NextFunction } from 'express';
import OpenAI from 'openai';
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

const openai = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY,
});

const router = Router();

// POST /api/classify (Protected)
router.post('/', isAuth as any, async (req: ClassifyRequest, res: Response) => {
  try {
    const { quizAnswers, goalText } = req.body;
    const userId = req.user?._id;

    if (!userId) {
      res.status(401).json({ message: 'User not authenticated' });
      return;
    }

    const systemPrompt = "You are a behavioral expert. Based on the user's goal and quiz, classify them as: Supporter, Challenger, or Pragmatist.\nReturn JSON: { 'persona': 'string', 'reasoning': 'string', 'theme_config': { 'color': 'hex' } }.\nBe strict with the JSON format.";

    const userPrompt = `Goal: ${goalText}\nQuiz Answers: ${JSON.stringify(quizAnswers)}`;

    const completion = await openai.chat.completions.create({
      model: "gpt-4o",
      messages: [
        { role: "system", content: systemPrompt },
        { role: "user", content: userPrompt }
      ],
      response_format: { type: "json_object" }
    });

    const result = JSON.parse(completion.choices[0].message.content || '{}') as PersonaResult;

    // Update User document with assigned persona
    await User.findByIdAndUpdate(userId, { persona: result.persona });

    res.json(result);
  } catch (err) {
    const error = err instanceof Error ? err.message : 'Unknown error';
    console.error('AI Classification Error:', error);
    res.status(500).json({ error: 'AI Classification failed' });
  }
});

export default router;
